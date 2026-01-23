package com.erp.domain.rent.controller;

import com.erp.domain.rent.dto.response.RentReturnResponse;
import com.erp.domain.rent.service.RentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/manager/rentals")
@RequiredArgsConstructor
public class RentStatusController {
    private final RentService rentService;

    // 차량 반납 처리
    @PatchMapping("/{rentId}/return")
    public ResponseEntity<RentReturnResponse> returnRental(@PathVariable Long rentId) {
        RentReturnResponse response = rentService.returnRental(rentId);

        return ResponseEntity.ok(response);
    }

    // 차량 대여 시작 처리
    @PatchMapping("/{rentId}/pickup")
    public ResponseEntity<Void> pickupRental(@PathVariable Long rentId) {
        rentService.startRental(rentId);

        return ResponseEntity.ok().build();
    }

}
