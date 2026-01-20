package com.erp.domain.branch.dto.response;

public record BranchResponse(
        Long branchId, // 지점 id
        String branchName, // 지점 이름
        Integer availableVehicles, // 이용 가능한 차량 수
        Double branchLatitude, // 지점 위도
        Double branchLongitude, // 지점 경도
        Double distance, // 유저와의 거리
        String branchAddress // 지점 주소
) {
}
