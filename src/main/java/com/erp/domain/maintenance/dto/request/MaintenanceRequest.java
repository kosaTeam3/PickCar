package com.erp.domain.maintenance.dto.request;

import com.erp.domain.maintenance.entity.MaintenanceStatus;

import java.time.LocalDate;

public record MaintenanceRequest(
        Long employeeId,
        String title,
        LocalDate maintenanceDate,
        Long cost,
        MaintenanceStatus status,
        String detail
) {
}
