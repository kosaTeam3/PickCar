package com.erp.domain.coupon.service;

import com.erp.domain.client.entity.Client;
import com.erp.domain.client.repository.ClientRepository;
import com.erp.domain.coupon.dto.request.CouponSaveRequest;
import com.erp.domain.coupon.entity.ClientCoupon;
import com.erp.domain.coupon.entity.Coupon;
import com.erp.domain.coupon.repository.ClientCouponRepository;
import com.erp.domain.coupon.repository.CouponRepository;
import com.erp.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
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
        } else {
            if (couponRepository.findByCode(couponCode).isPresent()) {
                throw new CustomException(404, "이미 존재하는 쿠폰 코드입니다.");
            }
        }

        Coupon coupon = Coupon.builder()
                .couponName(requestDto.couponName())
                .discount(requestDto.discount())
                .expDate(requestDto.expDate())
                .code(couponCode)
                .minPrice(requestDto.minPrice())
                .build();

        return couponRepository.save(coupon).getId();
    }

    private String generateRandomCode() {
        return UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    /* [사용자] 쿠폰 발급 */
    @Transactional
    public Long issueCoupon(Long clientId, String couponCode) {

        Client client = clientRepository.findById(clientId)
                .orElseThrow(() -> new CustomException(404, "존재하지 않는 회원입니다."));

        Coupon coupon = couponRepository.findByCode(couponCode)
                .orElseThrow(() -> new CustomException(404, "유효하지 않은 쿠폰 코드입니다."));

        if (coupon.getExpDate().isBefore(LocalDate.now())) {
            throw new CustomException(404, "만료된 쿠폰입니다.");
        }

        ClientCoupon clientCoupon = ClientCoupon.builder()
                .client(client)
                .coupon(coupon)
                .isUsed(false)
                .build();

        return clientCouponRepository.save(clientCoupon).getId();
    }

}
