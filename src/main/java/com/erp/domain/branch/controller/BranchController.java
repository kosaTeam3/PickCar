package com.erp.domain.branch.controller;

import com.erp.domain.branch.dto.request.CreateBranch;
import com.erp.domain.branch.dto.request.UpdateBranch;
import com.erp.domain.branch.service.BranchService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/manager/branches")
@RequiredArgsConstructor
public class BranchController {

    private final BranchService branchService;

    @PostMapping
    public ResponseEntity<Void> createBranch(@Valid @RequestBody CreateBranch dto) {
        branchService.createBranch(dto);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{branchId}")
    public ResponseEntity<Void> updateBranch(@Valid @PathVariable Long branchId, @RequestBody UpdateBranch dto) {
        branchService.updateBranch(branchId, dto);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{branchId}")
    public ResponseEntity<Void> deleteBranch(@PathVariable Long branchId) {
        branchService.deleteBranch(branchId);
        return ResponseEntity.noContent().build();
    }
}
