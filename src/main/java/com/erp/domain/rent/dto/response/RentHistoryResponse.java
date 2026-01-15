package com.erp.domain.rent.dto.response;

import java.time.LocalDateTime;

public record RentHistoryResponse(

        Long rentId,
        String clientName,
        String clientPhone,
        LocalDateTime startDate,
        LocalDateTime endDate

) {
}

