package com.erp.domain.branch.service;

import com.erp.domain.branch.dto.request.CreateBranch;
import com.erp.domain.branch.entity.Branch;
import com.erp.domain.branch.repository.BranchRepository;
import com.erp.domain.employee.entity.Employee;
import com.erp.domain.employee.repository.EmployeeRepository;
import com.erp.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
                .employCount(0)
                .carCount(0)
                .build();

        branchRepository.save(branch);
    }
}
