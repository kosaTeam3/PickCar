package com.erp.domain.payment.dto.response;

import com.erp.domain.payment.entity.PaymentStatus;
import lombok.Builder;

@Builder
public record PaymentSaveResponse(
        Long paymentId, // 결제 ID
        String merchantUid, // 생성한 주문 번호
        PaymentStatus status, // 결제 상태
        Long amount // 실제로 결제한 최종 금액
) {
}
