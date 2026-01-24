package com.erp.domain.rent.dto.request;

public record RentManagerSearchRequest(
        String status,
        Long branchId,
        String carNumber,
        String clientName
) {
}
