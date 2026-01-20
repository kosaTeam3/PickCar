package com.erp.domain.maintenance.dto.response;

import com.erp.domain.maintenance.entity.MaintenanceStatus;

public record MaintenanceListResponse(
        Long maintenanceId,
        Long carId,
        Long branchId,
        Long employeeId,
        String employeeName,
        String title,
        MaintenanceStatus status,
        String model,
        String brand,
        String vehicleIdNumber
) {
}
