package com.erp.domain.car.dto.request;

import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

// 이용 가능한 차량 조회를 위한 DTO
public record AvailableCarSearchRequest(
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
        LocalDateTime startRentDateTime,

        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
        LocalDateTime endRentDateTime
) {
}
