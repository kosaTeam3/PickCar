package com.erp.domain.client.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;

@Builder
public record RegisterClientRequestDto(
        @NotBlank(message = "이메일을 적어주세요")
        @Email(message = "이메일 형식이 아닙니다.")
        String email,

        @NotBlank(message = "비밀번호를 적어주세요 ")
        @Size(min = 8, max = 20, message = "비밀번호는 8자 이상 20자 이하로 입력해주세요")
        @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,20}$",
        message = "비밀번호는 영문, 숫자, 특수문자를 포함해야 합니다.")
        String password,

        @NotBlank(message = "휴대폰 번호")
        @Pattern(regexp = "^01(?:0|1|[6-9])-(?:\\d{3}|\\d{4})-\\d{4}$",
                message = "휴대폰 번호 형식이 올바르지 않습니다. (예 : 010-1234-5678)")
        String phoneNumber,

        @NotBlank(message = "이름")
        String name,

        // 주민 뒷번호 한국인 1~4 , 외국인 5~8
        @NotBlank(message = "주민번호")
        @Pattern(regexp = "^\\d{6}-[1-8]$", message = "주민번호 형식이 올바르지 않습니다.")
        String residentNumber,

        @NotBlank(message = "면허증 번호")
        @Pattern(regexp = "^\\d{2}-\\d{2}-\\d{6}-\\d{2}$",
                message = "면허증 번호 형식이 올바르지 않습니다. (예: 11-22-333333-44)\"")
        String licenceNumber,

        @NotBlank(message = "면허 발급처")
        String licenceArea,

        @NotBlank(message = "면허 발급일자")
        @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$",
                message = "발급일자는 YYYY-MM-DD 형식이어야 합니다.")
        String licenceDay
) {
}
