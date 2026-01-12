package com.erp.domain.client.dto.request;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

public record LoginRequestDto(


        String email,

        String password
) {}
