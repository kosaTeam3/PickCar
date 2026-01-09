package com.erp.domain.branch.service;

import com.erp.domain.branch.dto.request.CreateBranch;
import com.erp.domain.branch.dto.request.UpdateBranch;
import com.erp.domain.branch.dto.response.BranchList;
import com.erp.domain.branch.entity.Branch;
import com.erp.domain.branch.repository.BranchRepository;
import com.erp.domain.employee.entity.Employee;
import com.erp.domain.employee.repository.EmployeeRepository;
import com.erp.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
    public List<BranchList> branchNameList() {
        return branchRepository.findAllBranchName();
    }
}
