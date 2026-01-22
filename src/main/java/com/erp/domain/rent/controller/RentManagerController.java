package com.erp.domain.rent.controller;

import com.erp.domain.rent.dto.request.RentReturnRequest;
import com.erp.domain.rent.dto.response.RentHistoryResponse;
import com.erp.domain.rent.dto.response.RentReturnResponse;
import com.erp.domain.rent.service.RentService;
import com.sun.security.auth.UserPrincipal;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/manager/vehicles")
@RequiredArgsConstructor
public class RentManagerController {

    private final RentService rentService;

    /* 차량 대여 이력 조회 */
    @GetMapping("/rent/{carId}")
    public Page<RentHistoryResponse> getRentHistory(
            @PathVariable Long carId,
            @PageableDefault(size = 10) Pageable pageable
    ) {
        return rentService.getRentHistory(carId, pageable);
    }

    @PostMapping("/return")
    public ResponseEntity<RentReturnResponse> returnRent(
            @Valid @RequestBody RentReturnRequest request,
            @AuthenticationPrincipal UserPrincipal user
    ) {
        Long clientId = Long.parseLong(user.getName());

        RentReturnResponse response = rentService.returnRent(clientId, request);

        return ResponseEntity.ok(response);
    }
}
