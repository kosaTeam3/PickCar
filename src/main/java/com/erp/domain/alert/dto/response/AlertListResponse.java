package com.erp.domain.alert.dto.response;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record AlertListResponse(
        Long alertId,
        String type,
        String message,
        LocalDate date,
        boolean read,
        LocalDateTime createdAt
) {
}
