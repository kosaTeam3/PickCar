package com.erp.domain.branch.repository;

// 거리 계산 결과(distance)를 포함하는 Native Query 매핑을 위한 프로젝션 인터페이스
public interface BranchWithDistance {
    Long getId();
    String getName();
    Double getLatitude();
    Double getLongitude();
    Double getDistance(); // 유저와의 거리
}
