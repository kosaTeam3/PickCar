package com.erp.domain.rent.dto.request;

import jakarta.validation.constraints.NotBlank;

public record RentCancelRequest(
        @NotBlank(message = "취소 사유는 필수입니다.")
        String reason
) {
}
