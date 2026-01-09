package com.erp.domain.branch.dto.response;

import java.time.LocalDate;

public record BranchEmployeeList(
        Long id,
        String name,
        String phoneNumber,
        String email,
        String grade,
        LocalDate entryDate,
        LocalDate quitDate,
        String loginId
) {
}
