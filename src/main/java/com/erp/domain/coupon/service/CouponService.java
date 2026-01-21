package com.erp.domain.coupon.service;

import com.erp.domain.client.entity.Client;
import com.erp.domain.client.repository.ClientRepository;
import com.erp.domain.coupon.dto.request.CouponSaveRequest;
import com.erp.domain.coupon.dto.response.ClientCouponResponse;
import com.erp.domain.coupon.entity.ClientCoupon;
import com.erp.domain.coupon.entity.Coupon;
import com.erp.domain.coupon.entity.CouponStatus;
import com.erp.domain.coupon.repository.ClientCouponRepository;
import com.erp.domain.coupon.repository.CouponRepository;
import com.erp.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
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

    /* [사용자] 쿠폰 발급 */
    @Transactional
    public Long issueCoupon(Long clientId, String couponCode) {

        Client client = clientRepository.findById(clientId)
                .orElseThrow(() -> new CustomException(404, "존재하지 않는 회원입니다."));

        Coupon coupon = couponRepository.findAllByCode(couponCode)
                .orElseThrow(() -> new CustomException(404, "유효하지 않은 쿠폰 코드입니다."));

        LocalDate now = LocalDate.now();
        if (now.isBefore(coupon.getStartDate()) || now.isAfter(coupon.getEndDate())) {
            throw new CustomException(400, "지금은 쿠폰 발급 기간이 아닙니다. (발급 기간: "
                    + coupon.getStartDate() + " ~ " + coupon.getEndDate() + ")");
        }

        if (coupon.getMaxQuantity() <= coupon.getIssuedQuantity()) {
            throw new CustomException(400, "준비된 쿠폰 수량이 모두 소진되었습니다.");
        }

        if (clientCouponRepository.existsByClientIdAndCouponId(clientId, coupon.getId())) {
            throw new CustomException(400, "이미 발급된 쿠폰입니다.");
        }

        coupon.setIssuedQuantity(coupon.getIssuedQuantity() + 1);

        ClientCoupon clientCoupon = ClientCoupon.builder()
                .client(client)
                .coupon(coupon)
                .isUsed(false)
                .build();

        return clientCouponRepository.save(clientCoupon).getId();
    }

    /* 랜덤 코드 생성 */
    private String generateRandomCode() {
        return UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

}
