package com.erp.domain.client.dto.responce;

import java.time.LocalDateTime;

public record ClientSummaryResponse(
        Long clientId,
        String clientName,
        String clientEmail,
        String clientCall,
        LocalDateTime clientRegisterDate,
        Boolean blacklisted
) {
}
