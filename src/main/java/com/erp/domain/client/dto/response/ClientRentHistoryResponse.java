package com.erp.domain.client.dto.response;

import com.erp.domain.rent.entity.RentStatus;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record ClientRentHistoryResponse(
        Long id,
        String carName,
        String model,
        String brand,
        Integer year,
        RentStatus status,
        Long rentalFee, // 렌트 요금
        LocalDateTime startRentDateTime,
        LocalDateTime endRentDateTime
) {
}
