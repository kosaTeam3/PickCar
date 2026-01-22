package com.erp.domain.rent.dto.response;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record RentCancelResponse(
        String merchantUid,
        Long cancelAmount,
        LocalDateTime cancelledAt,
        String status
) {
}
