package com.erp.domain.branch.controller;

import com.erp.domain.branch.dto.request.BranchSearchRequest;
import com.erp.domain.branch.dto.response.BranchResponse;
import com.erp.domain.branch.service.BranchService;
import com.erp.domain.car.dto.request.AvailableCarSearchRequest;
import com.erp.domain.car.dto.response.AvailableCarResponse;
import com.erp.domain.car.service.CarService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/client/branches")
@RequiredArgsConstructor
public class ClientBranchController {
    private final BranchService branchService;
    private final CarService carService;

    @GetMapping
    public List<BranchResponse> getAvailableBranches(@ModelAttribute BranchSearchRequest request) {
        return branchService.getAvailableBranches(request);
    }

    @GetMapping("/{branchId}")
    public List<AvailableCarResponse> getAvailableCars(
            @PathVariable Long branchId,
            @ModelAttribute AvailableCarSearchRequest request) {
        return carService.getAvailableCarsByBranch(branchId, request);
    }

}
