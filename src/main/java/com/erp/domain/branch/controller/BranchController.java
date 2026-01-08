package com.erp.domain.branch.controller;

import com.erp.domain.branch.dto.request.CreateBranch;
import com.erp.domain.branch.service.BranchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/manager/branches")
@RequiredArgsConstructor
public class BranchController {

    private final BranchService branchService;

    @PostMapping
    public ResponseEntity<Void> createBranch(@RequestBody CreateBranch dto) {
        branchService.createBranch(dto);
        return ResponseEntity.noContent().build();
    }
}
