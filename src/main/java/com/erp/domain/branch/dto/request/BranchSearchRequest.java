package com.erp.domain.branch.dto.request;

import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

// 렌트 가능 지점 조회를 위한 요청 DTO
public record BranchSearchRequest(
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
        LocalDateTime startRentDateTime, // 렌트 시작 시각

        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
        LocalDateTime endRentDateTime, // 렌트 종료 시각

        Double userLatitude, // 유저 위도

        Double userLongitude // 유저 경도
) {
}
