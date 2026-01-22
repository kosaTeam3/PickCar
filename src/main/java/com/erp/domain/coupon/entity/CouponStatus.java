package com.erp.domain.coupon.entity;

public enum CouponStatus {
    READY,      // 발급 시작 전
    ACTIVE,     // 발금 가능
    EXHAUSTED,  // 수량 소진
    FINISHED,   // 발급 기간 종료
    EXPIRED     // 사용 만료 (expDate 지남)
}
