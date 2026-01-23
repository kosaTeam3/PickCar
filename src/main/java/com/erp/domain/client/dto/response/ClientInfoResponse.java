package com.erp.domain.client.dto.response;

import lombok.Builder;

@Builder
public record ClientInfoResponse(
        String name
) {
}
