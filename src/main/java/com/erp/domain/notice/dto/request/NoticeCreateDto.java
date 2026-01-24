package com.erp.domain.notice.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record NoticeCreateDto(
        @NotBlank String title,
        @NotBlank String content,
        @NotNull Boolean active, //강제 활성화
        @NotNull Boolean pinned,
        LocalDate startDate,
        LocalDate endDate
) {
}
