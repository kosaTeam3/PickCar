package com.erp.domain.coupon.controller;

import com.erp.domain.coupon.dto.request.CouponIssueRequest;
import com.erp.domain.coupon.dto.response.ClientCouponResponse;
import com.erp.domain.coupon.dto.response.EventCouponResponse;
import com.erp.domain.coupon.service.CouponService;
import com.sun.security.auth.UserPrincipal;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/client/coupons")
public class CouponController {

    private final CouponService couponService;

    /* [사용자] 쿠폰 발급 */
    @PostMapping("/issue")
    public Long issueCoupon(
            @AuthenticationPrincipal UserPrincipal user,
            @RequestBody @Valid CouponIssueRequest request
    ) {
        Long clientId = Long.parseLong(user.getName());

        return couponService.issueCoupon(clientId, request.code());
    }

    /* [사용자] 내 쿠폰 목록 조회 */
    @GetMapping
    public List<ClientCouponResponse> getMyCoupons(
            @AuthenticationPrincipal UserPrincipal user
    ) {
        Long clientId = Long.parseLong(user.getName());

        return couponService.getClientCoupons(clientId);
    }

    /* [사용자] 랜딩 페이지 이벤트 배너용 쿠폰 목록 */
    @GetMapping("/event")
    public List<EventCouponResponse> getEventCoupons() {
        return couponService.getActiveCoupons();
    }

}
