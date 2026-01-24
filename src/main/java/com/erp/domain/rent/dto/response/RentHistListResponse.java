package com.erp.domain.rent.dto.response;

import com.erp.domain.rent.entity.RentStatus;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record RentHistListResponse(
        Long carId,
        String stringImage,
        String model,
        String brand,
        Integer year,
        RentStatus status,
        Long branchId,
        LocalDateTime startRentDateTime,
        LocalDateTime endRentDateTime
) {
}
