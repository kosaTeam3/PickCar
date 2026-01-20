package com.erp.domain.statistics.dto;

public record CarStatisticsResponse(
        String brand,
        String model,
        Long count
) {
}
