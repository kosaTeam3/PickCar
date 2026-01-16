package com.erp.domain.alert.repository;

import com.erp.domain.alert.entity.Alert;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface AlertRepository extends JpaRepository<Alert, Long> {
    boolean existsByEmployeeIdAndTypeAndMessageAndDate(Long employeeId, String type, String message, LocalDate date);

     List<Alert> findByEmployeeIdOrderByCreatedAtDesc(Long employeeId);
}
