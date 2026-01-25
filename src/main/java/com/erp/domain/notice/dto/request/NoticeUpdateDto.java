package com.erp.domain.notice.dto.request;

import java.time.LocalDate;

public record NoticeUpdateDto(
        String title,
        String content,
        Boolean active,
        Boolean pinned,
        LocalDate startDate,
        LocalDate endDate
) {
}
