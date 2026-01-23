package com.erp.domain.client.dto.request;

import jakarta.validation.constraints.Email;
import lombok.Builder;

import java.time.LocalDate;

@Builder
public record MypageUpdateRequestDto(

        @Email(message = "이메일 형식이 아닙니다.")
        String email,
        String phoneNumber,
        LocalDate licenceDay
) {

}
