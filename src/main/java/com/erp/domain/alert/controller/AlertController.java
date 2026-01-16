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

    @GetMapping
    public ResponseEntity<List<AlertListResponse>> getAlerts(
            @RequestParam Long employeeId
    ) {
        return ResponseEntity.ok(alertService.getAlerts(employeeId));
    }

    @PatchMapping("/{alertId}")
    public ResponseEntity<Void> markAlertRead(@PathVariable Long alertId) {
        alertService.markAlertRead(alertId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{alertId}")
    public ResponseEntity<Void> deleteAlert(@PathVariable Long alertId) {
        alertService.deleteAlert(alertId);
        return ResponseEntity.noContent().build();
    }
}
