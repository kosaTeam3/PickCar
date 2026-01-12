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

        Branch branch = branchRepository.findById(request.branchId())
                .orElseThrow(() -> new CustomException(404, "해당 지점이 존재하지 않습니다."));

        car.setBranch(branch);
        car.setVehicleIdNumber(request.vehicleIdNumber());
        car.setModel(request.model());
        car.setPrice(request.price());
        car.setBrand(request.brand());
        car.setYear(request.year());
        car.setAgeLimit(request.ageLimit());
        car.setFuelType(request.fuelType());
        car.setImage(request.carImage());
        car.setCarNumber(request.carNumber());
        car.setMileage(request.mileage());
        car.setMaintenanceDate(request.maintenanceDate());
        car.setInsuranceName(request.insuranceName());
        car.setStatus(request.status());
        car.setPurchasePrice(request.purchasePrice());
        car.setModelPrice(request.modelPrice());
        car.setSeater(request.seater());
        car.setColor(request.color());
    }

    @Transactional
    public void deleteCar(Long carId) {
        Car car = carRepository.findById(carId)
                .orElseThrow(() -> new CustomException(404, "해당 차량이 존재하지 않습니다."));

        carRepository.delete(car);
    }
}