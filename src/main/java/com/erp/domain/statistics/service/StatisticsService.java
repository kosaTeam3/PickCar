package com.erp.domain.statistics.service;

import com.erp.domain.statistics.dto.StatisticsResponse;
import com.erp.domain.statistics.repository.StatisticsRepository;
import com.erp.global.exception.CustomException;
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
    public List<StatisticsResponse> getMonthlyRentStats(YearMonth start, YearMonth end) {

        LocalDateTime startDate = start.atDay(1).atStartOfDay();
        LocalDateTime endDate = end.atEndOfMonth().atTime(23, 59, 59);

        if (startDate.isAfter(endDate)) {
            throw new CustomException(400, "시작 날짜는 종료 날짜보다 빨라야 합니다.");
        }

        return statisticsRepository.findMonthlyRentCount(startDate, endDate)
                .stream()
                .map(row -> new StatisticsResponse(
                        row.getLabel(),
                        row.getCount()
                ))
                .toList();

    }

    /* 일간 대여 건수 통계 */
    public List<StatisticsResponse> getDailyRentStats(LocalDate start, LocalDate end) {

        LocalDateTime startDate = start.atStartOfDay();
        LocalDateTime endDate = end.atTime(23, 59, 59);

        if (startDate.isAfter(endDate)) {
            throw new CustomException(400, "시작 날짜는 종료 날짜보다 빨라야 합니다.");
        }

        return statisticsRepository.findDailyRentCount(startDate, endDate)
                .stream()
                .map(row -> new StatisticsResponse(
                        row.getLabel(),
                        row.getCount()))
                .toList();
    }

    /* 주간 대여 건수 통계 */
    public List<StatisticsResponse> getWeeklyRentStats(LocalDate start, LocalDate end) {

        LocalDateTime startDate = start.atStartOfDay();
        LocalDateTime endDate = end.atTime(23, 59, 59);

        if (startDate.isAfter(endDate)) {
            throw new CustomException(400, "시작 날짜는 종료 날짜보다 빨라야 합니다.");
        }

        return statisticsRepository.findWeeklyRentCount(startDate, endDate)
                .stream()
                .map(row -> new StatisticsResponse(
                        row.getLabel(),
                        row.getCount()))
                .toList();
    }

}
