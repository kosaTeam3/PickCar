package com.erp.domain.coupon.controller;

import com.erp.domain.coupon.dto.request.CouponSaveRequest;
import com.erp.domain.coupon.dto.response.AdminCouponResponse;
import com.erp.domain.coupon.service.CouponService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/manager/coupons") // 👈 관리자 전용 경로!
public class ManagerCouponController {

    private final CouponService couponService;

    /* [관리자] 쿠폰 생성 */
    @PostMapping
    public Long createCoupon(@RequestBody @Valid CouponSaveRequest request) {
        return couponService.createCoupon(request);
    }

    /* [관리자] 쿠폰 전체 목록 조회 */
    @GetMapping
    public List<AdminCouponResponse> getAllCoupons() {
        return couponService.getAdminCoupons();
    }
}