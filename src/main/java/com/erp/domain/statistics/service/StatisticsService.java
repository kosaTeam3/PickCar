package com.erp.domain.statistics.service;

import com.erp.domain.statistics.dto.StatisticsResponse;
import com.erp.domain.statistics.repository.StatisticsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StatisticsService {

    private final StatisticsRepository statisticsRepository;

    /* 월간 대여 건수 통계 */
    public List<StatisticsResponse> getMonthlyRentStats(String startMonth, String endMonth) {

        YearMonth start = YearMonth.parse(startMonth);
        LocalDateTime startDate = start.atDay(1).atStartOfDay();

        YearMonth end = YearMonth.parse(endMonth);
        LocalDateTime endDate = end.atEndOfMonth().atTime(23, 59, 59);

        return statisticsRepository.findMonthlyRentCount(startDate, endDate)
                .stream()
                .map(row -> new StatisticsResponse(
                        row.getLabel(),
                        row.getCount()
                ))
                .toList();

    }

    /* 일간 대여 건수 통계 */
    public List<StatisticsResponse> getDailyRentStats(String startDateStr, String endDateStr) {

        LocalDate start = LocalDate.parse(startDateStr);
        LocalDateTime startDate = start.atStartOfDay();

        LocalDate end = LocalDate.parse(endDateStr);
        LocalDateTime endDate = end.atTime(23, 59, 59);

        return statisticsRepository.findDailyRentCount(startDate, endDate)
                .stream()
                .map(row -> new StatisticsResponse(
                        row.getLabel(),
                        row.getCount()))
                .toList();
    }

    /* 주간 대여 건수 통계 */
    public List<StatisticsResponse> getWeeklyRentStats(String startDateStr, String endDateStr) {

        LocalDate start = LocalDate.parse(startDateStr);
        LocalDateTime startDate = start.atStartOfDay();

        LocalDate end = LocalDate.parse(endDateStr);
        LocalDateTime endDate = end.atTime(23, 59, 59);

        return statisticsRepository.findWeeklyRentCount(startDate, endDate)
                .stream()
                .map(row -> new StatisticsResponse(
                        row.getLabel(),
                        row.getCount()))
                .toList();
    }

}
