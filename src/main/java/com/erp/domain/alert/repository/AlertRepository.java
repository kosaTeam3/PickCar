package com.erp.domain.alert.repository;

import com.erp.domain.alert.entity.Alert;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;

public interface AlertRepository extends JpaRepository<Alert, Long> {
    boolean existsByEmployeeIdAndTypeAndMessageAndDate(Long employeeId, String type, String message, LocalDate date);

    Page<Alert> findAllByEmployeeId(Long employeeId, Pageable pageable);
}
