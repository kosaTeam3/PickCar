package com.erp.domain.client.dto.request;

import jakarta.validation.constraints.NotBlank;

public record ChangePasswordRequestDto(
        @NotBlank(message = "비밀번호를 적어주세요 ")
        String password
) {
}
