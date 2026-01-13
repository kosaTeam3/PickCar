package com.erp.domain.maintenance.controller;

import com.erp.domain.maintenance.dto.request.MaintenanceRequest;
import com.erp.domain.maintenance.dto.response.MaintenanceDetailResponse;
import com.erp.domain.maintenance.dto.response.MaintenanceListResponse;
import com.erp.domain.maintenance.entity.MaintenanceStatus;
import com.erp.domain.maintenance.service.MaintenanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/manager/maint")
@RequiredArgsConstructor
public class MaintenanceController {

    private final MaintenanceService maintenanceService;

    @PostMapping("/{carId}")
    public Long createMaintenance(@PathVariable Long carId, @RequestBody MaintenanceRequest request) {
        return maintenanceService.createMaintenance(carId, request);
    }

    @PatchMapping("/{maintenanceId}")
    public ResponseEntity<Void> updateMaintenance(@PathVariable Long maintenanceId, @RequestBody MaintenanceRequest request) {
        maintenanceService.updateMaintenance(maintenanceId, request);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{maintenanceId}")
    public ResponseEntity<Void> deleteMaintenance(@PathVariable Long maintenanceId) {
        maintenanceService.deleteMaintenance(maintenanceId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public Page<MaintenanceListResponse> getMaintenanceList(
            @PageableDefault(size = 20, sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
            @RequestParam(required = false) Long branchId,
            @RequestParam(required = false) MaintenanceStatus status,
            @RequestParam(required = false) String keyword
    ) {
        return maintenanceService.getMaintenanceList(pageable, branchId, status, keyword);
    }

    @GetMapping("/{maintenanceId}")
    public MaintenanceDetailResponse getMaintenanceDetail(@PathVariable Long maintenanceId) {
        return maintenanceService.getMaintenanceDetail(maintenanceId);
    }
}

