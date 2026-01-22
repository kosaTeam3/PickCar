package com.erp.domain.rent.dto.response;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record CurrentRentResponse(
        Long carId,             // 차량 ID
        String carImage,        // 사진
        String carModel,        // 차모델
        Long rentalFee,         // 최종 대여 금액(쿠폰 적용)
        String carBrand,        // 제조사
        Integer carYear,        // 제조년도
        LocalDateTime startRentDateTime, // 대여시작시각
        LocalDateTime endRentDateTime,   // 대여종료시각
        String branchName,      // 대여 지점 이름
        String branchAddress    // 대여 지점 주소
) {
}
