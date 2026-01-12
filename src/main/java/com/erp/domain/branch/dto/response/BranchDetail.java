package com.erp.domain.branch.dto.response;

import lombok.Builder;

@Builder
public record BranchDetail(
        Long branchId,
        String branchName,
        String branchPhoneNumber,
        String branchAddress,
        Long managerId,
        String managerName,
        Integer employeeCount,
        Integer carCount
) {
}
