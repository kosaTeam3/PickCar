package com.erp.domain.car.dto.request;

// 이용 가능한 차량 조회를 위한 DTO
public record AvailableCarSearchRequest(
        String startRentDateTime,
        String endRentDateTime
) {
}
