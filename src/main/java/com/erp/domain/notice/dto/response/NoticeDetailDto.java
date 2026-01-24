package com.erp.domain.notice.dto.response;

import lombok.Builder;

import java.time.LocalDate;

@Builder
public record NoticeDetailDto(
        Long id,
        Long employeeId,
        String employeeName,
        String title,
        String content,
        Boolean active,
        Boolean pinned,
        LocalDate startDate,
        LocalDate endDate
) {
}
