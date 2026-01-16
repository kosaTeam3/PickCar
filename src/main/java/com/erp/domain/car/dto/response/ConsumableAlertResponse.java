package com.erp.domain.car.dto.response;

public record ConsumableAlertResponse(
        String item,
        Long intervalKm,
        Long currentMileage,
        Long dueMileage,
        Long nextDueMileage,
        Long remainingKm,
        Long overdueKm,
        boolean due
) {
}
