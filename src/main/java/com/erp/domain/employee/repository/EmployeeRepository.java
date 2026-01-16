package com.erp.domain.employee.repository;

import com.erp.domain.employee.entity.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    <T> Slice<T> findByBranchId(Long branchId, Pageable pageable, Class<T> type);

    Page<Employee> findAllByQuitDateIsNull(Pageable pageable);

    // 이메일 중복검사
    boolean existsByEmail(String email);

    @Query(
            "SELECT e.loginId " +
                    "FROM Employee e " +
                    "WHERE e.loginId LIKE CONCAT(:prefix,'%')" +
                    "ORDER BY e.loginId DESC LIMIT 1"
    )
    Optional<String> findLastLoginId(@Param("prefix") String prefix);
}
