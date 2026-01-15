package com.erp.domain.car.service;

import com.erp.domain.alert.service.AlertService;
import com.erp.domain.branch.entity.Branch;
import com.erp.domain.branch.repository.BranchRepository;
import com.erp.domain.car.dto.request.CarCreateRequest;
import com.erp.domain.car.dto.request.CarSearchRequest;
import com.erp.domain.car.dto.request.CarUpdateRequest;
import com.erp.domain.car.dto.response.*;
import com.erp.domain.car.entity.Car;
import com.erp.domain.car.entity.CarStatus;
import com.erp.domain.car.repository.CarRepository;
import com.erp.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CarService {

    private final CarRepository carRepository;
    private final BranchRepository branchRepository;
    private final AlertService alertService;

    private static final List<ConsumableRule> CONSUMABLE_RULES = List.of(
            new ConsumableRule("엔진오일", 10_000L),
            new ConsumableRule("에어클리너", 20_000L),
            new ConsumableRule("에어컨 필터", 10_000L),
            new ConsumableRule("구동벨트", 80_000L),
            new ConsumableRule("변속기 오일(미션오일)", 100_000L),
            new ConsumableRule("부동액(냉각수)", 40_000L),
            new ConsumableRule("점화 플러그", 100_000L),
            new ConsumableRule("브레이크 패드", 50_000L),
            new ConsumableRule("브레이크 오일", 50_000L),
            new ConsumableRule("와이퍼 블레이드", 10_000L),
            new ConsumableRule("타이어", 50_000L)
    );

    @Transactional
    public Long createCar(CarCreateRequest request) {
        Branch branch = branchRepository.findById(request.branchId())
                .orElseThrow(() -> new CustomException(404, "해당 지점을 찾을 수 없습니다."));

        Car car = Car.builder()
                .branch(branch)
                .vehicleIdNumber(request.vehicleIdNumber())
                .model(request.model())
                .price(request.price())
                .brand(request.brand())
                .year(request.year())
                .ageLimit(request.ageLimit())
                .fuelType(request.fuelType())
                .image(request.carImage())
                .carNumber(request.carNumber())
                .mileage(request.mileage())
                .maintenanceDate(request.maintenanceDate())
                .insuranceName(request.insuranceName())
                .status(request.status())
                .purchasePrice(request.purchasePrice())
                .modelPrice(request.modelPrice())
                .seater(request.seater())
                .color(request.color())
                .build();

        Car savedCar = carRepository.save(car);
        alertService.createRegularInspectionAlerts(savedCar);
        return savedCar.getId();
    }

    @Transactional
    public void updateCar(Long carId, CarUpdateRequest request) {
        Car car = carRepository.findById(carId)
                .orElseThrow(() -> new CustomException(404, "해당 차량이 존재하지 않습니다."));

        CarStatus oldStatus = car.getStatus();

        if (request.vehicleIdNumber() != null) {
            car.setVehicleIdNumber(request.vehicleIdNumber());
        }

        if (request.model() != null) {
            car.setModel(request.model());
        }

        if (request.price() != null) {
            car.setPrice(request.price());
        }

        if (request.brand() != null) {
            car.setBrand(request.brand());
        }

        if (request.year() != null) {
            car.setYear(request.year());
        }

        if (request.ageLimit() != null) {
            car.setAgeLimit(request.ageLimit());
        }

        if (request.fuelType() != null) {
            car.setFuelType(request.fuelType());
        }

        if (request.carImage() != null) {
            car.setImage(request.carImage());
        }

        if (request.carNumber() != null) {
            car.setCarNumber(request.carNumber());
        }

        if (request.mileage() != null) {
            car.setMileage(request.mileage());
        }

        if (request.maintenanceDate() != null) {
            car.setMaintenanceDate(request.maintenanceDate());
        }

        if (request.insuranceName() != null) {
            car.setInsuranceName(request.insuranceName());
        }

        if (request.status() != null) {
            car.setStatus(request.status());
        }

        if (request.purchasePrice() != null) {
            car.setPurchasePrice(request.purchasePrice());
        }

        if (request.modelPrice() != null) {
            car.setModelPrice(request.modelPrice());
        }

        if (request.seater() != null) {
            car.setSeater(request.seater());
        }

        if (request.color() != null) {
            car.setColor(request.color());
        }

        if (request.branchId() != null) {
            Branch branch = branchRepository.findById(request.branchId())
                    .orElseThrow(() -> new CustomException(404, "해당 지점이 존재하지 않습니다."));
            car.setBranch(branch);
        }

        if (oldStatus == CarStatus.MAINTENANCE && car.getStatus() == CarStatus.WAITING) {
            // 기준점(lastMaintenanceMileage)을 현재 주행거리로 리셋하여 알람을 다음 주기로 밀어냄
            car.setLastMaintenanceMileage(car.getMileage());
            car.setMaintenanceDate(LocalDate.now());
        }

        // 3. 주행거리 업데이트 시 알람 생성 트리거 (정비 완료 직후라면 리셋된 기준으로 계산됨)
        if (request.mileage() != null) {
            List<String> dueItems = getDueConsumableItems(car);
            if (!dueItems.isEmpty()) {
                alertService.createConsumableAlerts(car, dueItems);
            }
        }

        if (request.branchId() != null) {
            Branch branch = branchRepository.findById(request.branchId())
                    .orElseThrow(() -> new CustomException(404, "해당 지점이 존재하지 않습니다."));
            car.setBranch(branch);
        }
    }

    @Transactional
    public void deleteCar(Long carId) {
        Car car = carRepository.findById(carId)
                .orElseThrow(() -> new CustomException(404, "해당 차량이 존재하지 않습니다."));

        carRepository.delete(car);
    }

    /* 전체 차량 목록 조회 */
    public Page<CarListResponse> getCarList(Pageable pageable) {
        return carRepository.findAll(pageable)
                .map(car -> CarListResponse.builder()
                        .carId(car.getId())
                        .vehicleIdNumber(car.getVehicleIdNumber())
                        .image(car.getImage())
                        .model(car.getModel())
                        .branchId(Optional.ofNullable(car.getBranch())
                                .map(Branch::getId).orElse(null))
                        .branchName(Optional.ofNullable(car.getBranch())
                                .map(Branch::getName).orElse(null))
                        .carNumber(car.getCarNumber())
                        .ageLimit(car.getAgeLimit())
                        .mileage(car.getMileage())
                        .status(car.getStatus().name())
                        .fuelType(car.getFuelType().name())
                        .seater(car.getSeater())
                        .color(car.getColor().name())
                        .build());

    }

    /* 차량 기본 정보 조회(상세조회) */
    public CarDetailResponse getCarDetail(Long carId) {

        Car car = carRepository.findById(carId)
                .orElseThrow(() -> new CustomException(404, "해당 차량이 존재하지 않습니다."));

        Long branchId = (car.getBranch() != null) ? car.getBranch().getId() : null;

        return CarDetailResponse.builder()
                .carId(car.getId())
                .vehicleIdNumber(car.getVehicleIdNumber())
                .model(car.getModel())
                .price(car.getPrice())
                .brand(car.getBrand())
                .year(car.getYear())
                .ageLimit(car.getAgeLimit())
                .fuelType(car.getFuelType().name())
                .image(car.getImage())
                .branchId(branchId)
                .carNumber(car.getCarNumber())
                .mileage(car.getMileage())
                .maintenanceDate(car.getMaintenanceDate())
                .insuranceName(car.getInsuranceName())
                .status(car.getStatus().name())
                .purchasePrice(car.getPurchasePrice())
                .modelPrice(car.getModelPrice())
                .seater(car.getSeater())
                .color(car.getColor().name())
                .build();
    }

    /* 차량 검색 (조건 필터 적용) */
    public Page<CarListResponse> searchCars(CarSearchRequest request, Pageable pageable) {

        Page<Car> cars = carRepository.searchCars(
                request.branchId(),
                request.brand(),
                request.model(),
                request.fuelType(),
                request.status(),
                pageable
        );

        return cars.map(car -> CarListResponse.builder()
                .carId(car.getId())
                .vehicleIdNumber(car.getVehicleIdNumber())
                .model(car.getModel())
                .image(car.getImage())
                .branchId(car.getBranch() != null ? car.getBranch().getId() : null)
                .branchName(car.getBranch() != null ? car.getBranch().getName() : null)
                .status(car.getStatus().name())
                .fuelType(car.getFuelType().name())
                .carNumber(car.getCarNumber())
                .ageLimit(car.getAgeLimit())
                .mileage(car.getMileage())
                .seater(car.getSeater())
                .color(car.getColor().name())
                .build());
    }

    public CarMaintenanceAlertResponse getMaintenanceAlerts(Long carId) {
        Car car = carRepository.findById(carId)
                .orElseThrow(() -> new CustomException(404, "해당 차량이 존재하지 않습니다."));

        String vehicleIdNumber = car.getVehicleIdNumber();

        Long mileage = Optional.ofNullable(car.getMileage()).orElse(0L);

        List<ConsumableAlertResponse> consumableAlerts = CONSUMABLE_RULES.stream()
                .map(rule -> toConsumableAlert(rule, car))
                .toList();

        RegularInspectionAlertResponse regularInspectionAlert = toRegularInspectionAlert(car);

        return new CarMaintenanceAlertResponse(
                car.getId(),
                vehicleIdNumber,
                mileage,
                consumableAlerts,
                regularInspectionAlert
        );
    }

    public List<String> getDueConsumableItems(Car car) {
        return CONSUMABLE_RULES.stream()
                .map(rule -> toConsumableAlert(rule, car))
                .filter(ConsumableAlertResponse::due)
                .map(ConsumableAlertResponse::item)
                .toList();
    }

    private ConsumableAlertResponse toConsumableAlert(ConsumableRule rule,Car car) {
        long interval = rule.intervalKm();
        long currentMileage = car.getMileage();
        long lastMileage = car.getLastMaintenanceMileage(); // 추가된 필드 사용

        // 1. 기준선 설정: 마지막 정비 지점 + 주기
        // 예: 10,500km에 엔진오일 갈았다면, 다음 기준선은 20,500km
        long dueMileage = lastMileage + interval;
        long nextDueMileage = dueMileage + interval;

        // 2. 남은 거리 및 초과 거리 계산
        long remainingKm = Math.max(dueMileage - currentMileage, 0);
        long overdueKm = Math.max(currentMileage - dueMileage, 0);

        // 3. 알림 조건 (정비 중 상태 반영)
        boolean isUnderMaintenance = car.getStatus() == CarStatus.MAINTENANCE;
        boolean due = !isUnderMaintenance && (currentMileage >= dueMileage);

        return new ConsumableAlertResponse(
                rule.item(),
                interval,
                currentMileage,
                dueMileage,
                nextDueMileage,
                remainingKm,
                overdueKm,
                due
        );
    }

    private RegularInspectionAlertResponse toRegularInspectionAlert(Car car) {
        // 1. 기준일 (정비날짜 우선, 없으면 생성일)
        LocalDate baseDate = (car.getMaintenanceDate() != null)
                ? car.getMaintenanceDate()
                : (car.getCreatedAt() != null ? car.getCreatedAt().toLocalDate() : LocalDate.now());

        LocalDate today = LocalDate.now();

        // 2. 가장 가까운 검사 예정일(미래) 찾기 (6개월 단위)
        LocalDate nextDueDate = baseDate.plusMonths(6);

        // 4. 일수 계산
        long daysBetween = ChronoUnit.DAYS.between(today, nextDueDate);
        long remainingDays = 0L;
        long overdueDays = 0L;

        if (daysBetween >= 0) {
            // 아직 날짜가 남았거나 오늘인 경우
            remainingDays = daysBetween;
            overdueDays = 0;
        } else {
            // 날짜가 지난 경우 (초과)
            remainingDays = 0;
            overdueDays = Math.abs(daysBetween); // 지나간 일수를 양수로 표현
        }

        // 5. [핵심] 알림 조건 판단
        // 차량 상태가 MAINTENANCE(정비 중)가 아닐 때만 알림을 보냄
        boolean isUnderMaintenance = "MAINTENANCE".equals(car.getStatus().name());

        // (7일 이내 임박했거나, 이미 날짜가 지났을 때) AND (정비 중이 아닐 때)
        boolean due = (remainingDays <= 7 || overdueDays > 0) && !isUnderMaintenance;

        return new RegularInspectionAlertResponse(
                baseDate,
                nextDueDate,
                remainingDays,
                overdueDays,
                due
        );
    }
    private record ConsumableRule(String item, long intervalKm) {
    }
}