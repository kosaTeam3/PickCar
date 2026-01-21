package com.erp.domain.payment.service;

import com.erp.domain.coupon.entity.ClientCoupon;
import com.erp.domain.coupon.repository.ClientCouponRepository;
import com.erp.domain.payment.dto.request.PaymentSaveRequest;
import com.erp.domain.payment.dto.response.PaymentSaveResponse;
import com.erp.domain.payment.entity.Payment;
import com.erp.domain.payment.entity.PaymentStatus;
import com.erp.domain.payment.repository.PaymentRepository;
import com.erp.domain.rent.entity.Rent;
import com.erp.domain.rent.entity.RentStatus;
import com.erp.domain.rent.repository.RentRepository;
import com.erp.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClient;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import java.time.LocalDateTime;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentService {  // 결제 내역 저장 및 검증
    private final PaymentRepository paymentRepository;
    private final RentRepository rentRepository;
    private final ClientCouponRepository clientCouponRepository;
    private final ObjectMapper objectMapper;

    @Value("${portone.api-key}")
    private String portOneApiKey;

    @Value("${portone.api-secret}")
    private String portOneApiSecret;

    private final RestClient restClient = RestClient.create();

    @Transactional
    public PaymentSaveResponse savePayment(PaymentSaveRequest request) {
        // 렌트 정보 조회
        Rent rent = rentRepository.findById(request.rentId())
                .orElseThrow(() -> new CustomException(404, "예약 정보를 찾을 수 없습니다."));

        // 이미 결제된 건인지 확인 (멱등성)
        if (paymentRepository.existsByImpUid(request.impUid())) {
            throw new CustomException(409, "이미 처리된 결제입니다.");
        }

        // 포트원 결제 내역 단건 조회 및 검증
        PortOnePaymentInfo paymentInfo = getPaymentInfoFromPortOne(request.impUid());

        // 결제 금액 검증 (DB 주문 금액 vs 실제 결제 금액)
        if (!rent.getRentalFee().equals(paymentInfo.amount())) {
            // 검증 실패 시 결제 취소 로직 (cancelPayment)을 호출하거나 예외 처리
            log.error("결제 금액 불일치: 주문금액({}), 결제금액({})", rent.getRentalFee(), paymentInfo.amount());
            throw new CustomException(400, "결제 금액이 일치하지 않습니다.");
        }

        // 결제 상태 확인
        if (!"paid".equals(paymentInfo.status())) {
            throw new CustomException(400, "결제가 완료되지 않았습니다. 상태: " + paymentInfo.status());
        }

        // Payment 엔티티 생성 및 저장
        Payment payment = Payment.builder()
                .rent(rent)
                .client(rent.getClient())
                .impUid(request.impUid())
                .merchantUid(request.merchantUid())
                .amount(paymentInfo.amount())
                .status(PaymentStatus.PAID)
                .paidAt(LocalDateTime.now()) // 실제로는 paymentInfo.paidAt() 변환 권장
                .paymentMethod(paymentInfo.payMethod())
                .pgProvider(paymentInfo.pgProvider())
                .build();

        Payment savedPayment = paymentRepository.save(payment);

        // 렌트 상태 변경 (WAITING_PAYMENT -> RESERVED)
        rent.setStatus(RentStatus.RESERVED);

        // 쿠폰을 사용했다면, 사용 확정 처리 (ClientCoupon 업데이트)
        if (rent.getClientCouponId() != null) {
            ClientCoupon clientCoupon = clientCouponRepository.findById(rent.getClientCouponId())
                    .orElseThrow(() -> new CustomException(404, "적용된 쿠폰 정보를 찾을 수 없습니다."));

            // 이미 사용된 쿠폰인 경우
            if (clientCoupon.isUsed()) {
                log.warn("이미 사용된 쿠폰입니다. RentId: {}, ClientCouponId: {}", rent.getId(), clientCoupon.getId());
            } else {
                // 사용 확정 업데이트
                clientCoupon.setUsed(true);
                clientCoupon.setUsedAt(LocalDateTime.now());
            }
        }

        return PaymentSaveResponse.builder()
                .paymentId(savedPayment.getId())
                .merchantUid(savedPayment.getMerchantUid())
                .status(savedPayment.getStatus())
                .amount(savedPayment.getAmount())
                .build();
    }

    // --- 포트원 API 연동 (Private Methods) ---

    // 액세스 토큰 발급
    private String getPortOneAccessToken() {
        try {
            String response = restClient.post()
                    .uri("https://api.iamport.kr/users/getToken")
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(Map.of("imp_key", portOneApiKey, "imp_secret", portOneApiSecret))
                    .retrieve()
                    .body(String.class);

            JsonNode root = objectMapper.readTree(response);

            if (root.get("code").asInt() != 0) {
                throw new CustomException(500, "포트원 토큰 발급 실패: " + root.get("message").asText());
            }

            return root.get("response").get("access_token").asText();

        } catch (Exception e) {
            log.error("PortOne Token Error", e);
            throw new CustomException(500, "결제 시스템 연동 중 오류가 발생했습니다.");
        }
    }

    // 결제 내역 조회
    private PortOnePaymentInfo getPaymentInfoFromPortOne(String impUid) {
        String accessToken = getPortOneAccessToken();

        try {
            String response = restClient.get()
                    .uri("https://api.iamport.kr/payments/" + impUid)
                    .header("Authorization", accessToken)
                    .retrieve()
                    .body(String.class);

            JsonNode root = objectMapper.readTree(response);
            if (root.get("code").asInt() != 0) {
                throw new CustomException(400, "유효하지 않은 결제 ID입니다.");
            }

            JsonNode paymentData = root.get("response");

            return new PortOnePaymentInfo(
                    paymentData.get("amount").asLong(),
                    paymentData.get("status").asText(),
                    paymentData.get("pay_method").asText(),
                    paymentData.get("pg_provider").asText()
            );

        } catch (Exception e) {
            log.error("PortOne Payment Info Error", e);
            throw new CustomException(500, "결제 정보 조회 중 오류가 발생했습니다.");
        }
    }
}
