package com.erp.domain.employee.dto;

import com.erp.domain.employee.entity.EmployeeAuthority;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.time.LocalDate;

@Builder
public record RegisterEmployeeRequestDto(

        @NotNull
        Long branchId,   // 소속 지점

        @NotBlank(message = "이름은 필수입니다")
        String name,

        @NotBlank(message = "연락처는 필수입니다.")
        String phoneNumber,

        @Email(message = "이메일 형식이 아닙니다.")
        @NotBlank(message = "이메일을 입력해주세요")
        String email,   // 사용자 ID

        @NotBlank(message = "직급을 선택해주세요")
        String grade,

        @NotNull(message = "권한을 선택해주세요")
        EmployeeAuthority authority,   // ADMIN, STAFF, MANAGER

        @NotNull(message = "입사일을 선택 ")  // 달력체크
        LocalDate entryDate
) {
}
