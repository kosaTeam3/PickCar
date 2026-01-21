package com.erp.domain.employee.repository;

import com.erp.domain.employee.entity.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    <T> Slice<T> findByBranchId(Long branchId, Pageable pageable, Class<T> type);

    @Query("""
                SELECT e
                FROM Employee e
                WHERE e.quitDate IS NULL
                    AND (:name     IS NULL OR e.name   LIKE %:name%)
                    AND (:email     IS NULL OR e.email       LIKE %:email%)\s
                    AND (:phone     IS NULL OR e.phoneNumber LIKE %:phone%)\s
                    AND (:grade     IS NULL OR e.grade       = :grade)\s
                    AND (:entryDate IS NULL OR e.entryDate   = :entryDate)
            
            """)
    Page<Employee> findAllByQuitDateIsNull(
            @Param("name") String name,
            @Param("email") String email,
            @Param("phone") String phone,
            @Param("grade") String grade,
            @Param("entryDate") LocalDate entryDate,
            Pageable pageable
    );

    @Query("""
                SELECT e
                FROM Employee e
                WHERE  (:name     IS NULL OR e.name   LIKE %:name%)
                    AND (:email     IS NULL OR e.email       LIKE %:email%)\s
                    AND (:phone     IS NULL OR e.phoneNumber LIKE %:phone%)\s
                    AND (:grade     IS NULL OR e.grade       = :grade)\s
                    AND (:entryDate IS NULL OR e.entryDate   = :entryDate)
            
            """)
    Page<Employee> findAllNotQuit(
            @Param("name") String name,
            @Param("email") String email,
            @Param("phone") String phone,
            @Param("grade") String grade,
            @Param("entryDate") LocalDate entryDate,
            Pageable pageable
    );

    Long countByBranch_Id(Long branchId);

    // 이메일 중복검사
    boolean existsByEmail(String email);

    List<Employee> findByBranchId(Long branchId);

    Optional<Employee> findByLoginId(String loginId);
}
