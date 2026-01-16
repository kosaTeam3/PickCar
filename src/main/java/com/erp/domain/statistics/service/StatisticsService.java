package com.erp.domain.statistics.service;

import com.erp.domain.statistics.dto.StatisticsResponse;
import com.erp.domain.statistics.repository.StatisticsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StatisticsService {

    private final StatisticsRepository rentStatisticsRepository;

    /* 월간 대여 건수 통계(최근 1년) */
    public List<StatisticsResponse> getMonthlyRentStats() {
        LocalDateTime oneYearAgo = LocalDateTime.now()
                .minusYears(1)
                .withDayOfMonth(1)
                .withHour(0).withMinute(0).withSecond(0);

        return rentStatisticsRepository.findMonthlyRentCount(oneYearAgo)
                .stream()
                .map(row -> new StatisticsResponse(
                        (String) row[0],
                        ((Number) row[1]).longValue()
                ))
                .toList();

    }
}
