package com.erp.domain.rent.controller;

import com.erp.domain.rent.dto.response.RentHistoryResponse;
import com.erp.domain.rent.service.RentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
