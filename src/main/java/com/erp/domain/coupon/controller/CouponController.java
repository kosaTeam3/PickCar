package com.erp.domain.coupon.controller;

import com.erp.domain.coupon.dto.request.CouponIssueRequest;
import com.erp.domain.coupon.dto.request.CouponSaveRequest;
import com.erp.domain.coupon.dto.response.ClientCouponResponse;
import com.erp.domain.coupon.service.CouponService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    /* [사용자] 쿠폰 등록 */
    @PostMapping("/issue")
    public Long issueCoupon(@RequestBody @Valid CouponIssueRequest request) {
        return couponService.issueCoupon(request.clientId(), request.code());
    }

    /* 내 쿠폰 목록 조회 */
    @GetMapping("/my/{clientId}")
    public List<ClientCouponResponse> getMyCoupons(@PathVariable Long clientId) {
        return couponService.getMyCoupons(clientId);
    }

}
