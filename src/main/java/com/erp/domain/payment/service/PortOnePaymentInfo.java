package com.erp.domain.payment.service;

public record PortOnePaymentInfo(
        // 서비스 계층에서 외부 API(포트원)와 통신하기 위해 사용하는 객체
        Long amount,
        String status,
        String payMethod,
        String pgProvider
) {
}
