package com.erp.domain.branch.dto.request;

public record UpdateBranch(

        String name,
        String phoneNumber,
        String address,
        Long employeeId,
        Double latitude,
        Double longitude
) {
}

