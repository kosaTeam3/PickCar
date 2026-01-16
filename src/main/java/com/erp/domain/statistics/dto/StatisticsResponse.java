package com.erp.domain.statistics.dto;

public record StatisticsResponse(
        String label,
        Long count
) {
}
