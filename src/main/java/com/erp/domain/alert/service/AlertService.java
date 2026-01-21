package com.erp.domain.alert.service;

import com.erp.domain.alert.dto.response.AlertListResponse;
import com.erp.domain.alert.entity.Alert;
import com.erp.domain.alert.repository.AlertRepository;
import com.erp.domain.branch.entity.Branch;
import com.erp.domain.car.entity.Car;
import com.erp.domain.employee.entity.Employee;
import com.erp.domain.employee.repository.EmployeeRepository;
import com.erp.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class AlertService {

    private static final String MAINTENANCE_TYPE = "MAINTENANCE";

    private final AlertRepository alertRepository;
    private final EmployeeRepository employeeRepository;

    /* 정기 점검 알림 */
    public void createRegularInspectionAlerts(Car car) {
        if (car.getCreatedAt() == null || car.getBranch() == null) {
            return;
        }

        LocalDate inspectionDate = getInspectionDueDate(car.getCreatedAt().toLocalDate(), LocalDate.now());
        String message = String.format("차량 %s 정기점검 예정일 (%s)", car.getCarNumber(), inspectionDate);

        createAlertsForBranch(car.getBranch(), message, inspectionDate);
    }

    /* 소모품 교체 알림 */
    public void createConsumableAlerts(Car car, List<String> items) {
        if (items.isEmpty() || car.getBranch() == null) {
            return;
        }

        LocalDate today = LocalDate.now();
        for (String item : items) {
            String message = String.format("차량 %s %s 교체 필요", car.getCarNumber(), item);
            createAlertsForBranch(car.getBranch(), message, today);
        }
    }

    /* 지점별 알림 배포 */
    private void createAlertsForBranch(Branch branch, String message, LocalDate date) {
        List<Employee> employees = employeeRepository.findByBranchId(branch.getId());
        for (Employee employee : employees) {
            if (!alertRepository.existsByEmployeeIdAndTypeAndMessageAndDate(employee.getId(), MAINTENANCE_TYPE, message, date)) {
                Alert alert = Alert.builder()
                        .employee(employee)
                        .type(MAINTENANCE_TYPE)
                        .message(message)
                        .date(date)
                        .build();
                alertRepository.save(alert);
            }
        }
    }

    private LocalDate getInspectionDueDate(LocalDate registrationDate, LocalDate today) {
        LocalDate firstDueDate = registrationDate.plusMonths(6);
        if (today.isBefore(firstDueDate)) {
            return firstDueDate;
        }

        long cycles = ChronoUnit.MONTHS.between(firstDueDate, today) / 6;
        return firstDueDate.plusMonths(cycles * 6);
    }

    @Transactional(readOnly = true)
    public List<AlertListResponse> getAlerts(Long employeeId) {
        return alertRepository.findByEmployeeIdOrderByCreatedAtDesc(employeeId).stream()
                .map(alert -> new AlertListResponse(
                        alert.getId(),
                        alert.getType(),
                        alert.getMessage(),
                        alert.getDate(),
                        alert.isRead(),
                        alert.getCreatedAt()
                ))
                .toList();
    }

    public void markAlertRead(Long alertId) {
        Alert alert = alertRepository.findById(alertId)
                .orElseThrow(() -> new CustomException(404, "해당 알림이 존재하지 않습니다."));

        alert.setRead(true);
        alertRepository.save(alert);
    }

    public void deleteAlert(Long alertId) {
        Alert alert = alertRepository.findById(alertId)
                .orElseThrow(() -> new CustomException(404, "해당 알림이 존재하지 않습니다."));

        alertRepository.delete(alert);
    }
}
