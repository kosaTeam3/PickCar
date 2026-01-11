package com.erp.domain.employee.repository;

import com.erp.domain.employee.entity.Employee;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    <T> Slice<T> findByBranchId(Long branchId, Pageable pageable, Class<T> type);
}
