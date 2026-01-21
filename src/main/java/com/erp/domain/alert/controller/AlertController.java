package com.erp.domain.alert.controller;

import com.erp.domain.alert.dto.response.AlertListResponse;
import com.erp.domain.alert.service.AlertService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/manager/alerts")
@RequiredArgsConstructor
public class AlertController {

    private final AlertService alertService;

    /* 알림 생성 */
    @GetMapping
    public ResponseEntity<List<AlertListResponse>> getAlerts(
            @RequestParam Long employeeId
    ) {
        return ResponseEntity.ok(alertService.getAlerts(employeeId));
    }

    /* 알림 상태 변경 */
    @PatchMapping("/{alertId}")
    public ResponseEntity<Void> markAlertRead(@PathVariable Long alertId) {
        alertService.markAlertRead(alertId);
        return ResponseEntity.noContent().build();
    }

    /* 알림 삭제 */
    @DeleteMapping("/{alertId}")
    public ResponseEntity<Void> deleteAlert(@PathVariable Long alertId) {
        alertService.deleteAlert(alertId);
        return ResponseEntity.noContent().build();
    }
}
