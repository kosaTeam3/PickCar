package com.erp.domain.statistics.controller;

import com.erp.domain.statistics.dto.StatisticsResponse;
import com.erp.domain.statistics.service.StatisticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/manager/statistics")
public class StatisticsController {

    private final StatisticsService statisticsService;

    /* 월간 대여 건수 조회 */
    @GetMapping("/case/month")
    public List<StatisticsResponse> getMonthlyCaseStats(
            @RequestParam String startMonth,
            @RequestParam String endMonth
    ) {
        return statisticsService.getMonthlyRentStats(startMonth, endMonth);
    }

    /* 일간 대여 건수 조회 */
    @GetMapping("/case/day")
    public List<StatisticsResponse> getDailyCaseStats(
            @RequestParam String startDate, // 예: 2026-01-01
            @RequestParam String endDate    // 예: 2026-01-31
    ) {
        return statisticsService.getDailyRentStats(startDate, endDate);
    }
}
