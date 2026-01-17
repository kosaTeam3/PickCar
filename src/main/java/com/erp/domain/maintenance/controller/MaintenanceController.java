package com.erp.domain.maintenance.controller;

import com.erp.domain.maintenance.dto.request.MaintenanceRequest;
import com.erp.domain.maintenance.dto.response.*;
import com.erp.domain.maintenance.entity.MaintenanceStatus;
import com.erp.domain.maintenance.service.MaintenanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/manager/maint")
@RequiredArgsConstructor
public class MaintenanceController {

    private final MaintenanceService maintenanceService;

    /* 정비 등록 */
    @PostMapping("/{carId}")
    public Long createMaintenance(@PathVariable Long carId, @RequestBody MaintenanceRequest request) {
        return maintenanceService.createMaintenance(carId, request);
    }

    /* 정비 내용 수정 */
    @PatchMapping("/{maintenanceId}")
    public ResponseEntity<Void> updateMaintenance(@PathVariable Long maintenanceId, @RequestBody MaintenanceRequest request) {
        maintenanceService.updateMaintenance(maintenanceId, request);
        return ResponseEntity.noContent().build();
    }
    /* 정비 내역 삭제 */
    @DeleteMapping("/{maintenanceId}")
    public ResponseEntity<Void> deleteMaintenance(@PathVariable Long maintenanceId) {
        maintenanceService.deleteMaintenance(maintenanceId);
        return ResponseEntity.noContent().build();
    }

    /* 정비 목록 조회 */
    @GetMapping
    public Page<MaintenanceListResponse> getMaintenanceList(
            @PageableDefault(size = 20, sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
            @RequestParam(required = false) Long branchId,
            @RequestParam(required = false) MaintenanceStatus status,
            @RequestParam(required = false) String keyword
    ) {
        return maintenanceService.getMaintenanceList(pageable, branchId, status, keyword);
    }

    /* 상태에 따른 정비 목록 조회 */
    @GetMapping("/grouped")
    public MaintenanceStatusGroupResponse getMaintenanceGroupedList(
            @RequestParam(required = false) Long branchId,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) MaintenancePeriod period,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate baseDate
    ) {
        return maintenanceService.getMaintenanceGroupedList(branchId, keyword, period, baseDate);
    }

    /* 상세 목록 조회 */
    @GetMapping("/{maintenanceId}")
    public MaintenanceDetailResponse getMaintenanceDetail(@PathVariable Long maintenanceId) {
        return maintenanceService.getMaintenanceDetail(maintenanceId);
    }

    /* 차량 정비 이력 조회 */
    @GetMapping("/maint/{carId}")
    public Page<MaintenanceHistoryResponse> getMaintenanceHistory(
            @PathVariable Long carId,
            @PageableDefault Pageable pageable
    ) {
        return maintenanceService.getMaintenanceHistory(carId, pageable);
    }

}

