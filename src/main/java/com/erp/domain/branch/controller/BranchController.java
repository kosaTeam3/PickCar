package com.erp.domain.branch.controller;

import com.erp.domain.branch.dto.request.CreateBranch;
import com.erp.domain.branch.dto.request.UpdateBranch;
import com.erp.domain.branch.dto.response.BranchDetail;
import com.erp.domain.branch.dto.response.BranchEmployeeList;
import com.erp.domain.branch.dto.response.BranchList;
import com.erp.domain.branch.dto.response.BranchNameList;
import com.erp.domain.branch.service.BranchService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping
    public Page<BranchList> getBranchList(@PageableDefault(size = 20) Pageable pageRequest) {
        return branchService.getBranchList(pageRequest);
    }

    @GetMapping("/{branchId}")
    public BranchDetail getBranchDetail(@PathVariable Long branchId) {
        return branchService.getBranchDetail(branchId);
    }

    @GetMapping("/list")
    public List<BranchNameList> branchNameList() {
        return branchService.branchNameList();
    }

    @GetMapping("/employees/{branchId}")
    public Slice<BranchEmployeeList> getBranchEmployeeList(@PageableDefault(size = 20) Pageable pageRequest, @PathVariable Long branchId) {
        return branchService.getBranchEmployeeList(pageRequest, branchId);
    }
}
