package com.erp.domain.coupon.dto.request;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

public record CouponSaveRequest(

        String code,

        @NotBlank(message = "쿠폰명은 필수 입력 항목입니다.")
        String couponName,

        @NotNull(message = "할인 금액은 필수 입력 항목입니다.")
        @Min(value = 0, message = "할인 금액은 0원 이상이어야 합니다.")
        Integer discount,

        @NotNull(message = "최대 발급 수는 필수 항목입니다. (0 입력 시 무제한)")
        @Min(value = 0, message = "최대 발급 수는 0 이상이어야 합니다.")
        Integer maxQuantity,

        @NotNull(message = "발급 시작일은 필수입니다.")
        LocalDate startDate,

        @NotNull(message = "발급 종료일은 필수입니다.")
        LocalDate endDate,

        @NotNull(message = "사용 만료일은 필수입니다.")
        @Future(message = "사용 만료일은 현재 날짜 이후여야 합니다.")
        LocalDate expDate
) {
}
