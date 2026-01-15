package com.erp.domain.employee.dto;

import com.erp.domain.employee.entity.EmployeeAuthority;
import jakarta.validation.constraints.Email;

import java.time.LocalDate;

public record UpdateEmployeeRequestDto(

        Long branchId,
        String name,
        String phoneNumber,
        @Email
        String email,
        String grade,
        EmployeeAuthority authority,
        LocalDate quitDate

) {
}
