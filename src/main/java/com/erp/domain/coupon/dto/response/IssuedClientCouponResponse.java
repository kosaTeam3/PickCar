package com.erp.domain.coupon.dto.response;

import java.time.LocalDateTime;

public record IssuedClientCouponResponse(

        String clientName,
        String email,
        String phoneNumber,
        LocalDateTime issuedAt,
        LocalDateTime usedAt,
        boolean isUsed
) {
}
