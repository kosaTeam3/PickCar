package com.erp.domain.coupon.dto.response;

import com.erp.domain.coupon.entity.CouponStatus;

import java.time.LocalDate;

public record AdminCouponResponse(
        Long couponId,
        String couponName,
        String code,
        Integer discount,
        Integer maxQuantity,
        Integer issuedQuantity,
        Long usedQuantity,
        LocalDate startDate,
        LocalDate endDate,
        LocalDate expDate,
        CouponStatus status
) {
}
