package com.erp.domain.maintenance.service;

import com.erp.domain.car.entity.Car;
import com.erp.domain.car.entity.CarStatus;
import com.erp.domain.car.repository.CarRepository;
import com.erp.domain.car.service.CarService;
import com.erp.domain.employee.entity.Employee;
import com.erp.domain.employee.repository.EmployeeRepository;
import com.erp.domain.maintenance.dto.request.MaintenanceRequest;
import com.erp.domain.maintenance.dto.response.*;
import com.erp.domain.maintenance.entity.Maintenance;
import com.erp.domain.maintenance.entity.MaintenanceStatus;
import com.erp.domain.maintenance.repository.MaintenanceRepository;
import com.erp.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.WeekFields;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MaintenanceService {

    private final MaintenanceRepository maintenanceRepository;
    private final CarRepository carRepository;
    private final EmployeeRepository employeeRepository;
    private final CarService carService;

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
                .consumables(resolveConsumables(car, request))
                .build();
        Maintenance savedMaintenance = maintenanceRepository.save(maintenance);
        applyCarMaintenanceStateIfCompleted(savedMaintenance, car);
        return savedMaintenance.getId();
    }

    @Transactional
    public void updateMaintenance(Long maintenanceId, MaintenanceRequest request) {
        Maintenance maintenance = maintenanceRepository.findById(maintenanceId)
                .orElseThrow(() -> new CustomException(404, "해당 정비 정보가 존재하지 않습니다."));

        MaintenanceStatus previousStatus = maintenance.getStatus();

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

        if (request.consumables() != null) {
            maintenance.setConsumables(resolveConsumables(maintenance.getCar(), request));
        }

        maintenanceRepository.save(maintenance);

        if (request.status() != null && request.status() != previousStatus) {
            applyCarMaintenanceStateIfCompleted(maintenance, maintenance.getCar());
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

    public MaintenanceStatusGroupResponse getMaintenanceGroupedList(Long branchId, String keyword, MaintenancePeriod period, LocalDate baseDate) {
        MaintenancePeriod resolvedPeriod = period != null ? period : MaintenancePeriod.MONTH;
        LocalDate resolvedBaseDate = baseDate != null ? baseDate : LocalDate.now();
        DateRange range = resolveRange(resolvedPeriod, resolvedBaseDate);

        List<MaintenanceGroupedItemResponse> items = maintenanceRepository.searchByPeriod(
                branchId,
                keyword,
                range.startDate(),
                range.endDate()
        );

        Map<MaintenanceStatus, List<MaintenanceGroupedItemResponse>> grouped = items.stream()
                .collect(Collectors.groupingBy(
                        MaintenanceGroupedItemResponse::status,
                        () -> new EnumMap<>(MaintenanceStatus.class),
                        Collectors.toList()
                ));

        List<MaintenanceGroupedItemResponse> scheduledList = grouped.getOrDefault(MaintenanceStatus.SCHEDULE, List.of());
        List<MaintenanceGroupedItemResponse> ongoingList = grouped.getOrDefault(MaintenanceStatus.ONGOING, List.of());
        List<MaintenanceGroupedItemResponse> completedList = grouped.getOrDefault(MaintenanceStatus.COMPLETED, List.of());

        return new MaintenanceStatusGroupResponse(
                resolvedPeriod,
                resolvedBaseDate,
                range.startDate(),
                range.endDate(),
                scheduledList.size(),
                ongoingList.size(),
                completedList.size(),
                grouped.getOrDefault(MaintenanceStatus.SCHEDULE, List.of()),
                grouped.getOrDefault(MaintenanceStatus.ONGOING, List.of()),
                grouped.getOrDefault(MaintenanceStatus.COMPLETED, List.of())
        );
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
                maintenance.getDetail(),
                parseConsumables(maintenance.getConsumables())
        );
    }

    private String resolveConsumables(Car car, MaintenanceRequest request) {
        List<String> requestedItems = request.consumables();
        List<String> dueItems = carService.getDueConsumableItems(car);

        if (requestedItems == null || requestedItems.isEmpty()) {
            return joinConsumables(dueItems);
        }

        List<String> filtered = requestedItems.stream()
                .filter(Objects::nonNull)
                .map(String::trim)
                .filter(item -> !item.isBlank())
                .filter(dueItems::contains)
                .toList();

        return joinConsumables(filtered);
    }

    private void applyCarMaintenanceStateIfCompleted(Maintenance maintenance, Car car) {
        if (maintenance.getStatus() != MaintenanceStatus.COMPLETED) {
            if (maintenance.getStatus() == MaintenanceStatus.ONGOING) {
                car.setStatus(CarStatus.MAINTENANCE);
            }
            return;
        }
        car.setStatus(CarStatus.WAITING);
        car.setLastMaintenanceMileage(car.getMileage());
        car.setMaintenanceDate(maintenance.getMaintenanceDate() != null ? maintenance.getMaintenanceDate() : LocalDate.now());
        carRepository.save(car);
    }

    private String joinConsumables(List<String> consumables) {
        if (consumables == null || consumables.isEmpty()) {
            return null;
        }
        return String.join(",", consumables);
    }

    private List<String> parseConsumables(String consumables) {
        if (consumables == null || consumables.isBlank()) {
            return Collections.emptyList();
        }
        return List.of(consumables.split(","));
    }

    private DateRange resolveRange(MaintenancePeriod period, LocalDate baseDate) {
        return switch (period) {
            case YEAR -> new DateRange(
                    baseDate.withDayOfYear(1),
                    baseDate.withDayOfYear(baseDate.lengthOfYear())
            );
            case MONTH -> new DateRange(
                    baseDate.withDayOfMonth(1),
                    baseDate.withDayOfMonth(baseDate.lengthOfMonth())
            );
            case WEEK -> {
                WeekFields weekFields = WeekFields.ISO;
                LocalDate start = baseDate.with(weekFields.dayOfWeek(), 1);
                LocalDate end = baseDate.with(weekFields.dayOfWeek(), 7);
                yield new DateRange(start, end);
            }
            case DAY -> new DateRange(baseDate, baseDate);
        };
    }

    private record DateRange(LocalDate startDate, LocalDate endDate) {
    }

    /* 차량 정비 이력 조회 */
    public Page<MaintenanceHistoryResponse> getMaintenanceHistory(Long carId, Pageable pageable) {

        Page<Maintenance> maintenanceList = maintenanceRepository.findMaintenanceHistory(
                carId,
                LocalDate.now(),
                pageable
        );

        return maintenanceList.map(m -> MaintenanceHistoryResponse.builder()
                .maintenanceId(m.getId())
                .title(m.getTitle())
                .detail(m.getDetail())
                .maintenanceDate(m.getMaintenanceDate())
                .cost(m.getCost())
                .employeeId(m.getEmployee().getId())
                .employeeName(m.getEmployeeName())
                .status(m.getStatus())
                .build()
        );
    }
}
