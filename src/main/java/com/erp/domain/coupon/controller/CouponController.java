package com.erp.domain.coupon.controller;

import com.erp.domain.coupon.dto.request.CouponIssueRequest;
import com.erp.domain.coupon.dto.request.CouponSaveRequest;
import com.erp.domain.coupon.service.CouponService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/coupons")
public class CouponController {

    private final CouponService couponService;

    /* [관리자] 쿠폰 생성 API */
    @PostMapping
    public Long createCoupon(@RequestBody @Valid CouponSaveRequest request) {
        return couponService.createCoupon(request);
    }

    /* [사용자] 쿠폰 발급 */
    @PostMapping("/issue")
    public Long issueCoupon(@RequestBody @Valid CouponIssueRequest request) {
        return couponService.issueCoupon(request.clientId(), request.couponCode());
    }

}
