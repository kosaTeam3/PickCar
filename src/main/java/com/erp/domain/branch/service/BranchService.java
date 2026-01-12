package com.erp.domain.branch.service;

import com.erp.domain.branch.dto.request.CreateBranch;
import com.erp.domain.branch.dto.request.UpdateBranch;
import com.erp.domain.branch.dto.response.BranchDetail;
import com.erp.domain.branch.dto.response.BranchEmployeeList;
import com.erp.domain.branch.dto.response.BranchList;
import com.erp.domain.branch.dto.response.BranchNameList;
import com.erp.domain.branch.entity.Branch;
import com.erp.domain.branch.repository.BranchRepository;
import com.erp.domain.employee.entity.Employee;
import com.erp.domain.employee.repository.EmployeeRepository;
import com.erp.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class BranchService {

    private final BranchRepository branchRepository;
    private final EmployeeRepository employeeRepository;

    public void createBranch(CreateBranch dto) {
        Employee manager = null;
        if (dto.employeeId() != null) {
            manager = employeeRepository.findById(dto.employeeId()).orElseThrow(
                    () -> new CustomException(404, "직원을 찾을 수 없습니다."));
        }

        Branch branch = Branch.builder()
                .name(dto.name())
                .phoneNumber(dto.phoneNumber())
                .address(dto.address())
                .manager(manager)
                .latitude(dto.latitude())
                .longitude(dto.longitude())
                .managerName(dto.employeeId() != null ? manager.getName() : null)
                .employeeCount(0)
                .carCount(0)
                .build();

        branchRepository.save(branch);
    }

    public void updateBranch(Long branchId, UpdateBranch dto) {
        Branch branch = branchRepository.findById(branchId).orElseThrow(
                () -> new CustomException(404, "지점을 찾을 수 없습니다.")
        );

        if (dto.employeeId() != null) {
            Employee manager = employeeRepository.findById(dto.employeeId()).orElseThrow(
                    () -> new CustomException(404, "직원을 찾을 수 없습니다.")
            );

            branch.setManager(manager);
            branch.setManagerName(manager.getName());
        }

        if (dto.name() != null) {
            branch.setName(dto.name());
        }
        if (dto.phoneNumber() != null) {
            branch.setPhoneNumber(dto.phoneNumber());
        }
        if (dto.address() != null) {
            branch.setAddress(dto.address());
        }
        if (dto.latitude() != null) {
            branch.setLatitude(dto.latitude());
        }
        if (dto.longitude() != null) {
            branch.setLongitude(dto.longitude());
        }
    }

    public void deleteBranch(Long branchId) {
        if (!branchRepository.existsById(branchId)) {
            throw new CustomException(404, "지점을 찾을 수 없습니다.");
        }

        branchRepository.deleteById(branchId);
    }

    @Transactional(readOnly = true)
    public List<BranchNameList> branchNameList() {
        return branchRepository.findAllBranchName();
    }

    @Transactional(readOnly = true)
    public BranchDetail getBranchDetail(Long branchId) {
        Branch branch = branchRepository.findById(branchId).orElseThrow(
                () -> new CustomException(404, "지점을 찾을 수 없습니다.")
        );

        Long managerId = (branch.getManager() != null) ? branch.getManager().getId() : null;

        return BranchDetail.builder()
                .branchId(branch.getId())
                .branchName(branch.getName())
                .branchPhoneNumber(branch.getPhoneNumber())
                .branchAddress(branch.getAddress())
                .managerId(managerId)
                .managerName(branch.getManagerName())
                .employeeCount(branch.getEmployeeCount())
                .carCount(branch.getCarCount())
                .build();
    }

    @Transactional(readOnly = true)
    public Page<BranchList> getBranchList(Pageable pageRequest) {
        return branchRepository.findAll(pageRequest).map(branch -> BranchList.builder()
                .branchId(branch.getId())
                .branchName(branch.getName())
                .branchPhoneNumber(branch.getPhoneNumber())
                .branchAddress(branch.getAddress())
                .managerId(Optional.ofNullable(branch.getManager())
                        .map(Employee::getId).orElse(null))
                .managerName(branch.getManagerName())
                .employeeCount(branch.getEmployeeCount())
                .carCount(branch.getCarCount())
                .build());
    }

    @Transactional(readOnly = true)
    public Slice<BranchEmployeeList> getBranchEmployeeList(Pageable pageRequest, Long branchId) {
        if (!branchRepository.existsById(branchId)) {
            throw new CustomException(404, "지점을 찾을 수 없습니다.");
        }

        return employeeRepository.findByBranchId(branchId, pageRequest, BranchEmployeeList.class);
    }
}
