package com.erp.domain.maintenance.dto.response;

import com.erp.domain.maintenance.entity.MaintenanceStatus;

import java.time.LocalDate;

public record MaintenanceGroupedItemResponse(
        Long maintenanceId,
        Long carId,
        Long branchId,
        Long employeeId,
        String employeeName,
        String title,
        MaintenanceStatus status,
        LocalDate maintenanceDate
) {
}
