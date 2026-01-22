package com.erp.domain.alert.controller;

import com.erp.domain.alert.dto.response.AlertListResponse;
import com.erp.domain.alert.service.AlertService;
import com.sun.security.auth.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/manager/alerts")
@RequiredArgsConstructor
public class AlertController {

    private final AlertService alertService;

    /* 알림 생성 */
    @GetMapping
    public ResponseEntity<Page<AlertListResponse>> getAlerts(
            @AuthenticationPrincipal UserPrincipal user,
            @PageableDefault(sort = "id", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        Long userId = Long.parseLong(user.getName());
        return ResponseEntity.ok(alertService.getAlerts(userId, pageable));
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
