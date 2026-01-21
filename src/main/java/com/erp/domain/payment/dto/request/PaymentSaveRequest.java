package com.erp.domain.payment.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record PaymentSaveRequest(
        @NotNull Long rentId,
        @NotBlank String impUid,      // 포트원 결제 고유 번호
        @NotBlank String merchantUid  // 주문 번호
) {
}
