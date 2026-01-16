package com.erp.domain.car.dto.response;

import lombok.Builder;

@Builder
public record CarStatusCountResponse(

        long total,
        long driving,
        long maintenance,
        long waiting
) {
}
