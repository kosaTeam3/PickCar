package com.erp.domain.client.dto.request;

import jakarta.validation.constraints.NotBlank;

public record AddClientBlacklist(
        @NotBlank String blacklistInfo
) {
}
