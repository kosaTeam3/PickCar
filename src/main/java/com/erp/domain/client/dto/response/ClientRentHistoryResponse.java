package com.erp.domain.client.dto.response;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record ClientRentHistoryResponse(
        Long id,
        String carName,
        String model,
        String brand,
        Integer year,
        Long rentalFee, // 렌트 요금
        LocalDateTime startRentDateTime,
        LocalDateTime endRentDateTime
) {
}
