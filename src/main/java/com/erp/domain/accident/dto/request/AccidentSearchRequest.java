package com.erp.domain.accident.dto.request;

import com.erp.domain.accident.entity.AccidentStatus;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

public record AccidentSearchRequest(
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
        LocalDateTime startAt,

        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
        LocalDateTime endAt,

        String vehicleIdNumber, // 차대번호 (부분 검색)
        String clientName,      // 고객명 (부분 검색)
        AccidentStatus status   // 사고 상태 (일치 검색)
) {
}
