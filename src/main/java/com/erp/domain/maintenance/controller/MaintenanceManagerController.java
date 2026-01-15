package com.erp.domain.maintenance.controller;

import com.erp.domain.maintenance.dto.response.MaintenanceHistoryResponse;
import com.erp.domain.maintenance.service.MaintenanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/manager/vehicles")
public class MaintenanceManagerController {

    private final MaintenanceService maintenanceService;

    /* 차량 정비 이력 조회 */
    @GetMapping("/maint/{carId}")
    public Page<MaintenanceHistoryResponse> getMaintenanceHistory(
            @PathVariable Long carId,
            @PageableDefault Pageable pageable
    ) {
        return maintenanceService.getMaintenanceHistory(carId, pageable);
    }
}
