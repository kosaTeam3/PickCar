package com.erp.domain.accident.dto.response;

import com.erp.domain.accident.entity.AccidentStatus;

import java.time.LocalDateTime;

public record AccidentResponse(
        Long accidentId,
        AccidentStatus accidentStatus,
        Long clientId,
        String clientName,
        LocalDateTime accidentTime,
        Long carId,
        String vehicleIdNumber,
        String brand,
        String model,
        Integer year
) {
}
