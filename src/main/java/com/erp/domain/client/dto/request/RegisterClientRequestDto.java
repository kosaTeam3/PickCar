package com.erp.domain.client.dto.request;

import com.erp.domain.client.entity.Gender;
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
        String password,

        @NotBlank(message = "휴대폰 번호")
        String phone_number,

        @NotBlank(message = "이름")
        String name,

        @NotBlank(message = "성별")
        Gender gender,

        @NotBlank(message = "생년월일")
        String birthday,

        // 주민 뒷번호 한국인 1~4 , 외국인 5~8
        @NotBlank(message = "주민번호")
        @Pattern(regexp = "^\\d{6}-[1-8]$", message = "주민번호 형식이 올바르지 않습니다.")
        String resident_number,

        @NotBlank(message = "면허증 번호")
        String licence_number,

        @NotBlank(message = "면허 발급처")
        String licence_area,

        @NotBlank(message = "면허 발급일자")
        String licence_day
) {
}
