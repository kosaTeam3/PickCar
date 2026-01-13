package com.erp.domain.maintenance.controller;

import com.erp.domain.maintenance.dto.request.MaintenanceRequest;
import com.erp.domain.maintenance.dto.response.MaintenanceDetailResponse;
import com.erp.domain.maintenance.dto.response.MaintenanceListResponse;
import com.erp.domain.maintenance.entity.MaintenanceStatus;
import com.erp.domain.maintenance.service.MaintenanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/manager/maint")
@RequiredArgsConstructor
public class MaintenanceController {

    private final MaintenanceService maintenanceService;

    @PostMapping("/{carId}")
    public ResponseEntity<Long> createMaintenance(@PathVariable Long carId, @RequestBody MaintenanceRequest request) {
        Long maintenanceId = maintenanceService.createMaintenance(carId, request);
        return ResponseEntity.ok(maintenanceId);
    }

    @PatchMapping("/{carId}")
    public ResponseEntity<Void> updateMaintenance(@PathVariable Long carId, @RequestBody MaintenanceRequest request) {
        maintenanceService.updateMaintenance(carId, request);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{carId}")
    public ResponseEntity<Void> deleteMaintenance(@PathVariable Long carId) {
        maintenanceService.deleteMaintenance(carId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public Page<MaintenanceListResponse> getMaintenanceList(
            @PageableDefault(size = 20) Pageable pageable,
            @RequestParam(required = false) Long branchId,
            @RequestParam(required = false) MaintenanceStatus status,
            @RequestParam(required = false) String keyword
    ) {
        return maintenanceService.getMaintenanceList(pageable, branchId, status, keyword);
    }

    @GetMapping("/{carId}")
    public MaintenanceDetailResponse getMaintenanceDetail(@PathVariable Long carId) {
        return maintenanceService.getMaintenanceDetail(carId);
    }
}

