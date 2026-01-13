package com.erp.domain.branch.dto.request;

// 렌트 가능 지점 조회를 위한 요청 DTO
public record BranchSearchRequest(
        String startRentDateTime, // 렌트 시작 시각
        String endRentDateTime, // 렌트 종료 시각
        Double userLatitude, // 유저 위도
        Double userLongitude // 유저 경도
) {
}
