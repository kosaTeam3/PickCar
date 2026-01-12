package com.erp.domain.client.dto.request;


public record LoginRequestDto(

        String email,
        String password
) {
}
