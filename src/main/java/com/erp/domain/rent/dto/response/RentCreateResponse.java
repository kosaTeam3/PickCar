package com.erp.domain.rent.dto.response;

import lombok.Builder;

@Builder
public record RentCreateResponse(
        Long rentId,
        String merchantUid, // 결제 요청용 주문 번호
        Long amount, // 결제해야 할 총 금액
        String carModel,
        String buyerName,
        String buyerEmail,
        String buyerPhone
) {
}
