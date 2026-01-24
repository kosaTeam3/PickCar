package com.erp.domain.rent.dto.response;

import com.erp.domain.car.entity.CarStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record RentManagerListResponse(
        Long rentId,
        Long carId,
        String carNumber,
        String model,
        String clientName,
        String clientPhone,
        CarStatus carStatus,

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
        LocalDateTime startRentDateTime,

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
        LocalDateTime endRentDateTime
) {
}
