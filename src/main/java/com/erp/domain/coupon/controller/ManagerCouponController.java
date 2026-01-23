package com.erp.domain.coupon.controller;

import com.erp.domain.coupon.dto.request.CouponSaveRequest;
import com.erp.domain.coupon.dto.response.AdminCouponResponse;
import com.erp.domain.coupon.dto.response.IssuedClientCouponResponse;
import com.erp.domain.coupon.service.CouponService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/manager/coupons")
public class ManagerCouponController {

    private final CouponService couponService;

    /* [관리자] 쿠폰 생성 */
    @PostMapping
    public Long createCoupon(@RequestBody @Valid CouponSaveRequest request) {
        return couponService.createCoupon(request);
    }

    /* [관리자] 쿠폰 전체 목록 조회 */
    @GetMapping
    public Page<AdminCouponResponse> getAllCoupons(
            @PageableDefault(page = 0, size = 5, sort = "id", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        return couponService.getAdminCoupons(pageable);
    }

    /* [관리자] 특정 쿠폰 발급 유저 목록 조회 */
    @GetMapping("/{couponId}/clients")
    public Page<IssuedClientCouponResponse> getIssuedClients(
            @PathVariable Long couponId,
            @PageableDefault(page = 0, size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        return couponService.getIssuedClients(couponId, pageable);
    }
}