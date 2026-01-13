package com.erp.domain.branch.controller;

import com.erp.domain.branch.dto.request.BranchSearchRequest;
import com.erp.domain.branch.dto.response.BranchResponse;
import com.erp.domain.branch.service.BranchService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/client/branches")
@RequiredArgsConstructor
public class ClientBranchController {
    private final BranchService branchService;

    @GetMapping
    public List<BranchResponse> getAvailableBranches(@ModelAttribute BranchSearchRequest request) {
        return branchService.getAvailableBranches(request);
    }

}
