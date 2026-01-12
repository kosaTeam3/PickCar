package com.erp.domain.car.service;

import com.erp.domain.branch.entity.Branch;
import com.erp.domain.branch.repository.BranchRepository;
import com.erp.domain.car.dto.request.CarCreateRequest;
import com.erp.domain.car.dto.request.CarUpdateRequest;
import com.erp.domain.car.entity.Car;
import com.erp.domain.car.repository.CarRepository;
import com.erp.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CarService {

    private final CarRepository carRepository;
    private final BranchRepository branchRepository;

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
}