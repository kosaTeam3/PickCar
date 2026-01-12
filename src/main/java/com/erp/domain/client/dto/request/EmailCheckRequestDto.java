package com.erp.domain.client.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record EmailCheckRequestDto(
        @NotBlank(message = "이메일을 입력해주세요")
        @Email(message = "이메일 형식이 아닙니다.")
        String email

) {
}
