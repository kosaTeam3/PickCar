package com.erp.domain.statistics.controller;

import com.erp.domain.statistics.dto.StatisticsResponse;
import com.erp.domain.statistics.service.StatisticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/manager/statistics")
public class StatisticsController {

    private final StatisticsService statisticsService;

    /* 월간 대여 건수 조회 */
    @GetMapping("/case/month")
    public List<StatisticsResponse> getMonthlyCaseStats(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM") YearMonth startMonth,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM") YearMonth endMonth
    ) {
        return statisticsService.getMonthlyRentStats(startMonth, endMonth);
    }

    /* 일간 대여 건수 조회 */
    @GetMapping("/case/day")
    public List<StatisticsResponse> getDailyCaseStats(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate
    ) {
        return statisticsService.getDailyRentStats(startDate, endDate);
    }

    /* 주간 대여 건수 조회 */
    @GetMapping("/case/week")
    public List<StatisticsResponse> getWeeklyCaseStats(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate
    ) {
        return statisticsService.getWeeklyRentStats(startDate, endDate);
    }
}
