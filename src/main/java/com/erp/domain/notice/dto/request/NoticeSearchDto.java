package com.erp.domain.notice.dto.request;

public record NoticeSearchDto(
        String title,
        String writer,
        Boolean active
) {
}
