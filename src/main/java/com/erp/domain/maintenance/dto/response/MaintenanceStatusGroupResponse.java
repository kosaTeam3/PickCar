package com.erp.domain.maintenance.dto.response;

import java.time.LocalDate;
import java.util.List;

public record MaintenanceStatusGroupResponse(
        MaintenancePeriod period,
        LocalDate baseDate,
        LocalDate startDate,
        LocalDate endDate,
        int scheduledCount,
        int ongoingCount,
        int completedCount,
        List<MaintenanceGroupedItemResponse> schedule,
        List<MaintenanceGroupedItemResponse> ongoing,
        List<MaintenanceGroupedItemResponse> completed
) {
}
