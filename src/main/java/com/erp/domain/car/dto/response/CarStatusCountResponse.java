package com.erp.domain.car.dto.response;

import lombok.Builder;

@Builder
public record CarStatusCountResponse(

        Long total,
        Long driving,
        Long maintenance,
        Long waiting
) {
}
