package com.erp.domain.maintenance.dto.response;

import com.erp.domain.maintenance.entity.MaintenanceStatus;
import lombok.Builder;

import java.time.LocalDate;

@Builder
public record MaintenanceHistoryResponse(

        Long maintenanceId,
        String title,
        String detail,
        LocalDate maintenanceDate,
        Long cost,
        Long employeeId,
        String employeeName,
        MaintenanceStatus status
) {
}
