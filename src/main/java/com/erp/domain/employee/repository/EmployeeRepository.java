package com.erp.domain.employee.repository;

import com.erp.domain.employee.entity.Employee;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.swing.text.html.Option;
import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    <T> Slice<T> findByBranchId(Long branchId, Pageable pageable, Class<T> type);

    // 이메일 중복검사
    boolean existsByEmail(String email);

    Optional<Employee> findTopByLoginIdStartingWithOrderByLoginIdDesc(String prefix);
}
