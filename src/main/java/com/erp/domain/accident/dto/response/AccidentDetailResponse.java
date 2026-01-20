package com.erp.domain.accident.dto.response;

import com.erp.domain.accident.entity.AccidentStatus;
import java.time.LocalDateTime;

public record AccidentDetailResponse(
        Long accidentId,
        AccidentStatus accidentStatus,
        String accidentDescription,
        String accidentLocation,
        LocalDateTime accidentTime,
        String accidentPart,
        Long repairCost,
        Long clientLiability,
        Long clientId,
        String clientName,
        Long carId,
        String insuranceName,
        String vehicleIdNumber,
        String brand,
        String model,
        Integer year
) {
}
