package com.erp.domain.car.dto.response;

import java.util.List;

public record CarMaintenanceAlertResponse(
        Long carId,
        String vehicleIdNumber,
        Long currentMileage,
        List<ConsumableAlertResponse> consumables,
        RegularInspectionAlertResponse regularInspection
) {
}
