package com.erp.domain.employee.dto.request;


public record EmployeeLoginRequestDto(

        String loginId,
        String password
) {
}
