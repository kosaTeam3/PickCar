package com.erp.domain.statistics.controller;

import com.erp.domain.statistics.dto.StatisticsResponse;
import com.erp.domain.statistics.service.StatisticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/manager/statistics")
public class StatisticsController {

    private final StatisticsService statisticsService;

    /* 월간 대여 건수 조회 */
    @GetMapping("/case/month")
    public List<StatisticsResponse> getMonthlyCaseStats() {
        return statisticsService.getMonthlyRentStats();
    }
}
