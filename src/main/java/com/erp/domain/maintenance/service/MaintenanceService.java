package com.erp.domain.maintenance.service;

import com.erp.domain.car.entity.Car;
import com.erp.domain.car.repository.CarRepository;
import com.erp.domain.employee.entity.Employee;
import com.erp.domain.employee.repository.EmployeeRepository;
import com.erp.domain.maintenance.dto.request.MaintenanceRequest;
import com.erp.domain.maintenance.dto.response.MaintenanceDetailResponse;
import com.erp.domain.maintenance.dto.response.MaintenanceListResponse;
import com.erp.domain.maintenance.entity.Maintenance;
import com.erp.domain.maintenance.entity.MaintenanceStatus;
import com.erp.domain.maintenance.repository.MaintenanceRepository;
import com.erp.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MaintenanceService {

    private final MaintenanceRepository maintenanceRepository;
    private final CarRepository carRepository;
    private final EmployeeRepository employeeRepository;

    @Transactional
    public Long createMaintenance(Long carId, MaintenanceRequest request) {
        Car car = carRepository.findById(carId)
                .orElseThrow(() -> new CustomException(404, "해당 차량이 존재하지 않습니다."));

        Employee employee = employeeRepository.findById(request.employeeId())
                .orElseThrow(() -> new CustomException(404, "해당 직원이 존재하지 않습니다."));

        Maintenance maintenance = Maintenance.builder()
                .branch(car.getBranch())
                .car(car)
                .employee(employee)
                .employeeName(employee.getName())
                .vehicleIdNumber(car.getVehicleIdNumber())
                .title(request.title())
                .maintenanceDate(request.maintenanceDate())
                .cost(request.cost())
                .status(request.status())
                .detail(request.detail())
                .build();

        return maintenanceRepository.save(maintenance).getId();
    }

    @Transactional
    public void updateMaintenance(Long maintenanceId, MaintenanceRequest request) {
        Maintenance maintenance = maintenanceRepository.findById(maintenanceId)
                .orElseThrow(() -> new CustomException(404, "해당 정비 정보가 존재하지 않습니다."));

        if (request.employeeId() != null) {
            Employee employee = employeeRepository.findById(request.employeeId())
                    .orElseThrow(() -> new CustomException(404, "해당 직원이 존재하지 않습니다."));
            maintenance.setEmployee(employee);
            maintenance.setEmployeeName(employee.getName());
        }

        if (request.title() != null) {
            maintenance.setTitle(request.title());
        }

        if (request.maintenanceDate() != null) {
            maintenance.setMaintenanceDate(request.maintenanceDate());
        }

        if (request.cost() != null) {
            maintenance.setCost(request.cost());
        }

        if (request.status() != null) {
            maintenance.setStatus(request.status());
        }

        if (request.detail() != null) {
            maintenance.setDetail(request.detail());
        }
        maintenanceRepository.save(maintenance);
    }

    @Transactional
    public void deleteMaintenance(Long maintenanceId) {
        Maintenance maintenance = maintenanceRepository.findById(maintenanceId)
                .orElseThrow(() -> new CustomException(404, "해당 정비 정보가 존재하지 않습니다."));

        maintenanceRepository.delete(maintenance);
    }

    public Page<MaintenanceListResponse> getMaintenanceList(Pageable pageable, Long branchId, MaintenanceStatus status, String keyword) {
        return maintenanceRepository.search(branchId, status, keyword, pageable);
    }

    public MaintenanceDetailResponse getMaintenanceDetail(Long maintenanceId) {
        Maintenance maintenance = maintenanceRepository.findById(maintenanceId)
                .orElseThrow(() -> new CustomException(404, "해당 정비 정보가 존재하지 않습니다."));

        return new MaintenanceDetailResponse(
                maintenance.getId(),
                maintenance.getCar().getId(),
                maintenance.getBranch().getId(),
                maintenance.getEmployee().getId(),
                maintenance.getEmployeeName(),
                maintenance.getVehicleIdNumber(),
                maintenance.getTitle(),
                maintenance.getMaintenanceDate(),
                maintenance.getCost(),
                maintenance.getStatus(),
                maintenance.getDetail()
        );
    }
}
