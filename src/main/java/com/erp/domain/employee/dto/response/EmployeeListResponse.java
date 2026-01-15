package com.erp.domain.employee.dto.response;

import lombok.Builder;

import java.time.LocalDate;

@Builder
public record EmployeeListResponse(

        Long employId,
        String employName,
        String employCall,
        String employGrade,
        Long branchId,
        LocalDate entryDate,
        LocalDate quitDate,
        String employEmail,
        String loginId
) {
}
