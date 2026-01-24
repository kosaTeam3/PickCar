package com.erp.domain.coupon.service;

import com.erp.domain.client.entity.Client;
import com.erp.domain.client.repository.ClientRepository;
import com.erp.domain.coupon.dto.request.CouponSaveRequest;
import com.erp.domain.coupon.dto.response.AdminCouponResponse;
import com.erp.domain.coupon.dto.response.ClientCouponResponse;
import com.erp.domain.coupon.dto.response.EventCouponResponse;
import com.erp.domain.coupon.dto.response.IssuedClientCouponResponse;
import com.erp.domain.coupon.entity.ClientCoupon;
import com.erp.domain.coupon.entity.Coupon;
import com.erp.domain.coupon.entity.CouponStatus;
import com.erp.domain.coupon.repository.ClientCouponRepository;
import com.erp.domain.coupon.repository.CouponRepository;
import com.erp.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CouponService {

    private final CouponRepository couponRepository;
    private final ClientCouponRepository clientCouponRepository;
    private final ClientRepository clientRepository;

    /* [관리자] 쿠폰 생성 */
    @Transactional
    public Long createCoupon(CouponSaveRequest requestDto) {

        String couponCode = requestDto.code();
        if (couponCode == null || couponCode.isBlank()) {
            couponCode = generateRandomCode();
        } else if (couponRepository.findAllByCode(couponCode).isPresent()) {
            throw new CustomException(400, "이미 존재하는 쿠폰 코드입니다.");
        }

        Coupon coupon = Coupon.builder()
                .couponName(requestDto.couponName())
                .discount(requestDto.discount())
                .code(couponCode)
                .maxQuantity(requestDto.maxQuantity())
                .issuedQuantity(0)
                .startDate(requestDto.startDate())
                .endDate(requestDto.endDate())
                .expDate(requestDto.expDate())
                .build();

        return couponRepository.save(coupon).getId();
    }

    /* [관리자] 쿠폰 전체 목록 조회 */
    public Page<AdminCouponResponse> getAdminCoupons(Pageable pageable) {

        LocalDate now = LocalDate.now();

        Page<Coupon> couponPage = couponRepository.findAll(pageable);

        return couponPage.map(coupon -> {
            // 관리자용 상태 동적 계산 (우선순위 : 기간 전 -> 종료 -> 만료 -> 소진 -> 정상)
            CouponStatus status;
            if (now.isBefore(coupon.getStartDate())) {
                status = CouponStatus.READY;         // 발급 시작 전
            } else if (now.isAfter(coupon.getEndDate())) {
                status = CouponStatus.FINISHED;     // 발급 기간 종료
            } else if (now.isAfter(coupon.getExpDate())) {
                status = CouponStatus.EXPIRED;      // 사용 기한 만료
            } else if (coupon.getMaxQuantity() != 0 && coupon.getIssuedQuantity() >= coupon.getMaxQuantity()) {
                status = CouponStatus.EXHAUSTED;    // 수량 소진
            } else {
                status = CouponStatus.ACTIVE;       // 발급 중
            }

            // 실사용 횟수 조회
            long usedCount = clientCouponRepository.countByCouponIdAndIsUsedTrue(coupon.getId());

            return new AdminCouponResponse(
                    coupon.getId(),
                    coupon.getCouponName(),
                    coupon.getCode(),
                    coupon.getDiscount(),
                    coupon.getMaxQuantity(),
                    coupon.getIssuedQuantity(),
                    usedCount,
                    coupon.getStartDate(),
                    coupon.getEndDate(),
                    coupon.getExpDate(),
                    status
            );
        });
    }

    /* [관리자] 특정 쿠폰 발급 유저 목록 조회 */
    public Page<IssuedClientCouponResponse> getIssuedClients(Long couponId, Pageable pageable) {

        return clientCouponRepository.findByCouponId(couponId, pageable)
                .map(clientCoupon -> new IssuedClientCouponResponse(
                        clientCoupon.getClient().getName(),
                        clientCoupon.getClient().getEmail(),
                        clientCoupon.getClient().getPhoneNumber(),
                        clientCoupon.getCreatedAt(),
                        clientCoupon.getUsedAt(),
                        clientCoupon.isUsed()
                ));
    }

    /* [사용자] 쿠폰 발급 */
    @Transactional
    public Long issueCoupon(Long clientId, String couponCode) {

        Client client = clientRepository.findById(clientId)
                .orElseThrow(() -> new CustomException(404, "존재하지 않는 회원입니다."));

        Coupon coupon = couponRepository.findAllByCode(couponCode)
                .orElseThrow(() -> new CustomException(404, "유효하지 않은 쿠폰 코드입니다."));

        // 발급 유효성 검증 (기간 -> 수량 -> 중복 여부 순서)
        LocalDate now = LocalDate.now();
        if (now.isBefore(coupon.getStartDate()) || now.isAfter(coupon.getEndDate())) {
            throw new CustomException(400, "지금은 쿠폰 발급 기간이 아닙니다. (발급 기간: "
                    + coupon.getStartDate() + " ~ " + coupon.getEndDate() + ")");
        }

        // 수량 제한 확인 (0이면 무제한이므로 체크 안함, maxQuantity가 0이 아닐때만 체크)
        if (coupon.getMaxQuantity() <= coupon.getIssuedQuantity()) {
            throw new CustomException(400, "준비된 쿠폰 수량이 모두 소진되었습니다.");
        }

        if (clientCouponRepository.existsByClientIdAndCouponId(clientId, coupon.getId())) {
            throw new CustomException(400, "이미 발급된 쿠폰입니다.");
        }

        // 발급 수량 증가
        coupon.setIssuedQuantity(coupon.getIssuedQuantity() + 1);

        ClientCoupon clientCoupon = ClientCoupon.builder()
                .client(client)
                .coupon(coupon)
                .isUsed(false)
                .build();

        return clientCouponRepository.save(clientCoupon).getId();
    }

    /* [사용자] 내 쿠폰 목록 조회 */
    public List<ClientCouponResponse> getClientCoupons(Long clientId) {
        LocalDate now = LocalDate.now();

        return clientCouponRepository.findByClientId(clientId).stream()
                .map(clientCoupon -> {
                    Coupon coupon = clientCoupon.getCoupon();

                    String status = "ACTIVE"; // 기본은 사용 가능
                    if (clientCoupon.isUsed()) {
                        status = "USED";      // 사용 완료
                    } else if (now.isAfter(coupon.getExpDate())) {
                        status = "EXPIRED";   // 기간 만료
                    }

                    return new ClientCouponResponse(
                            clientCoupon.getId(),
                            coupon.getCouponName(),
                            coupon.getDiscount(),
                            coupon.getExpDate(),
                            coupon.getCode(),
                            status
                    );
                })
                .toList();
    }

    /* 이벤트 배너용 쿠폰 목록 조회 */
    public List<EventCouponResponse> getActiveCoupons() {
        LocalDate today = LocalDate.now();

        List<Coupon> activeCoupons = couponRepository.findActiveCoupons(today);

        return activeCoupons.stream()
                .map(coupon -> new EventCouponResponse(
                        coupon.getId(),
                        coupon.getCode(),
                        coupon.getCouponName(),
                        coupon.getMaxQuantity() - coupon.getIssuedQuantity(),
                        coupon.getStartDate(),
                        coupon.getEndDate(),
                        coupon.getExpDate()
                ))
                .toList();
    }

    /* 랜덤 코드 생성 (8자리 대문자) */
    private String generateRandomCode() {
        return UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

}
