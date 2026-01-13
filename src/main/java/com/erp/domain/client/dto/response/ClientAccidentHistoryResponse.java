package com.erp.domain.client.dto.response;

import com.erp.domain.accident.entity.AccidentStatus;

import java.time.LocalDateTime;

public record ClientAccidentHistoryResponse(
        Long id,
        LocalDateTime accidentTime,
        AccidentStatus accidentStatus,
        String accidentDetail,
        String accidentLocate,
        String accidentPart,
        String accidentImage,
        String insuranceInfo,
        String brand,
        String model,
        String year
) {
}
