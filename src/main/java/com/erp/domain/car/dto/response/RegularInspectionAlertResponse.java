package com.erp.domain.car.dto.response;

import java.time.LocalDate;

public record RegularInspectionAlertResponse(
        LocalDate registrationDate,
        LocalDate nextInspectionDate,
        Long remainingDays,
        Long overdueDays,
        boolean due
) {
}
