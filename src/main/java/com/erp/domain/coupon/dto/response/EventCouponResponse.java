package com.erp.domain.coupon.dto.response;

import java.time.LocalDate;

public record EventCouponResponse(

        Long clientId,
        String code,
        String couponName,
        Integer remainQuantity,
        LocalDate startDate,
        LocalDate endDate,
        LocalDate expDate
) {
}
