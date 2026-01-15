package com.erp.domain.car.service;

import com.erp.domain.branch.entity.Branch;
import com.erp.domain.branch.repository.BranchRepository;
import com.erp.domain.car.dto.request.AvailableCarSearchRequest;
import com.erp.domain.car.dto.request.CarCreateRequest;
import com.erp.domain.car.dto.request.CarSearchRequest;
import com.erp.domain.car.dto.request.CarUpdateRequest;
import com.erp.domain.car.dto.response.AvailableCarResponse;
import com.erp.domain.car.dto.response.CarDetailResponse;
import com.erp.domain.car.dto.response.CarListResponse;
import com.erp.domain.car.entity.Car;
import com.erp.domain.car.entity.CarStatus;
import com.erp.domain.car.repository.CarRepository;
import com.erp.domain.rent.repository.RentRepository;
import com.erp.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CarService {

    private final CarRepository carRepository;
    private final BranchRepository branchRepository;
    private final RentRepository rentRepository;

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

        return carRepository.save(car).getId();
    }

    @Transactional
    public void updateCar(Long carId, CarUpdateRequest request) {
        Car car = carRepository.findById(carId)
                .orElseThrow(() -> new CustomException(404, "해당 차량이 존재하지 않습니다."));

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

    public List<AvailableCarResponse> getAvailableCarsByBranch(Long branchId, AvailableCarSearchRequest request) {
        LocalDateTime startRentDateTime = request.startRentDateTime();
        LocalDateTime endRentDateTime = request.endRentDateTime();

        // 해당 기간에 예약된 차량 ID 조회
        List<Long> rentedCarIds = rentRepository.findRentedCarIds(startRentDateTime, endRentDateTime);

        List<Car> availableCars = carRepository.findAvailableCars(
                branchId,
                CarStatus.WAITING,
                rentedCarIds.isEmpty() ? null : rentedCarIds
        );

        return availableCars.stream()
                .map(car -> new AvailableCarResponse(
                        car.getId(),
                        car.getImage(),
                        car.getModel(),
                        car.getPrice(),
                        car.getBrand(),
                        car.getYear(),
                        car.getAgeLimit(),
                        car.getFuelType(),
                        car.getSeater(),
                        car.getColor()
                ))
                .toList();
    }

    /* 차량 검색 (조건 필터 적용) */
    @Transactional(readOnly = true)
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
}