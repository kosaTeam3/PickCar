package com.erp.domain.rent.dto.response;

import com.erp.domain.rent.entity.RentStatus;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record RentReturnResponse(
        Long rentId,
        RentStatus rentStatus,
        LocalDateTime returnDateTime,
        String message
) {
}
