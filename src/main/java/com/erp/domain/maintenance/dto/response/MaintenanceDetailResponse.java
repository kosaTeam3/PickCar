package com.erp.domain.maintenance.dto.response;

import com.erp.domain.maintenance.entity.MaintenanceStatus;

import java.time.LocalDate;
import java.util.List;

public record MaintenanceDetailResponse(
        Long maintenanceId,
        Long carId,
        Long branchId,
        Long employeeId,
        String employeeName,
        String vehicleIdNumber,
        String title,
        LocalDate maintenanceDate,
        Long cost,
        MaintenanceStatus status,
        String detail,
        List<String> consumables
) {
}
