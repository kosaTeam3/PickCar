package com.erp.domain.rent.dto.request;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record RentCreateRequest(
        @NotNull Long carId,
        @NotNull LocalDateTime startRentDateTime,
        @NotNull LocalDateTime endRentDateTime,
        Long clientCouponId // Nullable
) {
}
