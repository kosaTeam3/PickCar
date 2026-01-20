package com.erp.domain.coupon.dto.request;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

public record CouponSaveRequest(

        @NotBlank(message = "쿠폰명은 필수 입력 항목입니다.")
        String couponName,

        @NotNull(message = "할인금액은 필수 입력 항목입니다.")
        @Min(value = 1000, message = "할인금액은 1000원 이상이어야 합니다.")
        Integer discount,

        @NotNull(message = "유효기간은 필수 입력 항목입니다.")
        @Future(message = "유효기간은 현재 날짜 이후여야 합니다.")
        @DateTimeFormat(pattern = "yyyy-MM-dd")
        LocalDate expDate,
        String code,

        @Min(value = 1000, message = "최소 구매 금액은 1000원 이상이어야 합니다.")
        Integer minPrice
) {
}
