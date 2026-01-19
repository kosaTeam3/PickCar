package com.erp.domain.accident.entity;

public enum AccidentStatus {
    REPORT, // 사고 접수
    REPAIRING, // 수리 입고
    REPAIRED, // 수리 완료
    SUIT, // 소송중
    COMPLETED // 처리 완료
}