package com.erp.domain.client.dto.request;

public record ReissueRequestDto(

        String accessToken,
        String refreshToken
) {
}
