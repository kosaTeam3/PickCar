package com.erp.domain.coupon.dto.request;

public record CouponIssueRequest(

    Long clientId,
    String code
) {
}
