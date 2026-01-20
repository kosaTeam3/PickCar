package com.erp.domain.rent.entity;

public enum RentStatus {
    WAITING_PAYMENT, // 결제 대기
    RESERVED,        // 예약 확정 (결제 완료)
    CANCELLED,       // 취소됨
    COMPLETED        // 이용 완료
}
