package com.erp.domain.accident.dto.request;

import com.erp.domain.accident.entity.AccidentStatus;

import java.time.LocalDateTime;

public record AccidentRequest(
        AccidentStatus status, // 사고 진행 상황
        String description, // 사고 설명
        String location, // 대략적인 사고 위치
        LocalDateTime time, // 사고 시각
        String part, // 사고 부위
        Long repairCost, // 수리비
        Long clientLiability // 고객 부담금
) {
}
