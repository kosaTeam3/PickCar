package com.erp.domain.client.dto.response;

import lombok.Builder;

import java.time.LocalDate;

@Builder
public record MypageResponse(

        String name,
        String email,
        String phoneNumber,
        String licenceNumber,
        String licenceArea,
        LocalDate licenceDay
) {

}
