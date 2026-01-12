package com.erp.domain.branch.dto.request;

import jakarta.validation.constraints.NotNull;

public record CreateBranch(

        Long employeeId,
        @NotNull String name,
        @NotNull String phoneNumber,
        @NotNull String address,
        @NotNull Double latitude,
        @NotNull Double longitude
) {
}

