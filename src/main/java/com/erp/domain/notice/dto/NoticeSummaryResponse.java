package com.erp.domain.notice.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record NoticeSummaryResponse(
        Long id,
        String employeeName,
        String title,
        String summary,
        Boolean active,
        Boolean pinned,
        LocalDate startDate,
        LocalDate endDate,
        LocalDateTime createdAt
) {
}
