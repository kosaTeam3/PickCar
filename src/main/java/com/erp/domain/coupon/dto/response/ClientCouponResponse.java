package com.erp.domain.coupon.dto.response;

import java.time.LocalDate;

public record ClientCouponResponse(

        Long clientCouponId,
        String couponName,
        Integer discount,
        LocalDate expDate,
        String code,
        String status
) {
}
