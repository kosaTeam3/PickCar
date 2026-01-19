package com.erp.domain.employee.dto.request;

import java.time.LocalDate;

public record EmployeeSearchRequest(
        String name,
        String email,
        String call,  // DB phoneNumber와 매핑
        String grade,
        LocalDate entryDate
) {
}
