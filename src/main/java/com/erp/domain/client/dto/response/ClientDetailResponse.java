package com.erp.domain.client.dto.response;

import com.erp.domain.client.entity.Gender;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record ClientDetailResponse(
        Long clientId,
        String clientName,
        String clientEmail,
        String clientCall,
        LocalDate clientBirthday,
        Gender gender,
        String driverLicenceNumber,
        LocalDateTime clientRegisterDate,
        LocalDate licenceDay,
        Boolean blacked,
        String blackedInfo
) {
}
