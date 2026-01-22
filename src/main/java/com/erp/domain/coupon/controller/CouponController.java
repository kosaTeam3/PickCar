package com.erp.domain.coupon.controller;

import com.erp.domain.coupon.dto.request.CouponIssueRequest;
import com.erp.domain.coupon.dto.response.ClientCouponResponse;
import com.erp.domain.coupon.service.CouponService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/client/coupons")
public class CouponController {

    private final CouponService couponService;

    /* [사용자] 쿠폰 발급 */
    @PostMapping("/issue")
    public Long issueCoupon(@RequestBody @Valid CouponIssueRequest request) {
        return couponService.issueCoupon(request.clientId(), request.code());
    }

    /* [사용자] 내 쿠폰 목록 조회 */
    @GetMapping("/{clientId}")
    public List<ClientCouponResponse> getMyCoupons(@PathVariable Long clientId) {
        return couponService.getClientCoupons(clientId);
    }

}
