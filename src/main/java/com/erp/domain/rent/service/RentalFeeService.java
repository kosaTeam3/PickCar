package com.erp.domain.rent.service;

import com.erp.domain.car.entity.Car;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Service
public class RentalFeeService {

    private static final int DAILY_CAP_HOUR_THRESHOLD = 10; // 일일 상한 시간 (10시간)

    // 최종 렌트 요금을 계산하는 로직
    public Long calculateRentalFee(Car car, long totalHours) {
        long days = totalHours / 24;
        long remainingHours = totalHours % 24;
        Long dailyCapFee = getDailyCapFee(car); // 일일 상한 요금 (1일당 요금)
        long totalFee = 0L;

        if (days > 0) {
            totalFee += days * dailyCapFee;
        }

        // 24시간 단위로 나누어 떨어지지 않는 남은 시간에 대한 요금 계산
        long hourlySum = remainingHours * car.getPrice();

        totalFee += Math.min(hourlySum, dailyCapFee);

        return totalFee;
    }

    // 대여 시간 계산: 분 단위 올림을 통해 시간 단위로 환산하며, 비정상적인 분 단위 접근을 방어
    public long calculateRentalHours(LocalDateTime startDateTime, LocalDateTime endDateTime) {
        long minutes = ChronoUnit.MINUTES.between(startDateTime, endDateTime);

        return (long) Math.ceil(minutes / 60.0);
    }

    // 일일 상한 요금 계산
    public Long getDailyCapFee(Car car) {
        if (car.getPrice() == null) {
            return 0L;
        }

        return car.getPrice() * DAILY_CAP_HOUR_THRESHOLD;
    }
}