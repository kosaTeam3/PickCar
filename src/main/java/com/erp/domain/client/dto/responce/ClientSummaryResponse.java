package com.erp.domain.client.dto.responce;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record ClientSummaryResponse(
        Long clientId,
        @NotNull String clientName,
        @NotNull String clientEmail,
        @NotNull String clientCall,
        LocalDateTime clientRegisterDate,
        Boolean blacklisted
) {
}
