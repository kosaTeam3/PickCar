package com.erp.domain.car.service;

import com.erp.domain.branch.entity.Branch;
import com.erp.domain.branch.repository.BranchRepository;
import com.erp.domain.car.dto.request.CarCreateRequest;
import com.erp.domain.car.entity.Car;
import com.erp.domain.car.repository.CarRepository;
import jakarta.persistence.EntityNotFoundException;
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
        Branch branch = branchRepository.findById(request.getBranchId())
                .orElseThrow(() -> new IllegalArgumentException("해당 지점을 찾을 수 없습니다."));

        Car car = Car.builder()
                .branch(branch)
                .vehicleIdNumber(request.getVehicleIdNumber())
                .model(request.getModel())
                .price(request.getPrice())
                .brand(request.getBrand())
                .year(request.getYear())
                .ageLimit(request.getAgeLimit())
                .fuelType(request.getFuelType())
                .image(request.getCarImage())
                .carNumber(request.getCarNumber())
                .mileage(request.getMileage())
                .maintenanceDate(request.getMaintenanceDate())
                .insuranceName(request.getInsuranceName())
                .status(request.getStatus())
                .purchasePrice(request.getPurchasePrice())
                .modelPrice(request.getModelPrice())
                .seater(request.getSeater())
                .color(request.getColor())
                .build();

        return carRepository.save(car).getId();
    }

    @Transactional
    public void updateCar(Long carId, CarCreateRequest request) {
        Car car = carRepository.findById(carId)
                .orElseThrow(() -> new EntityNotFoundException("해당 차량이 존재하지 않습니다."));

        car.setVehicleIdNumber(request.getVehicleIdNumber());
        car.setModel(request.getModel());
        car.setPrice(request.getPrice());
        car.setBrand(request.getBrand());
        car.setYear(request.getYear());
        car.setAgeLimit(request.getAgeLimit());
        car.setFuelType(request.getFuelType());
        car.setImage(request.getCarImage());
        car.setCarNumber(request.getCarNumber());
        car.setMileage(request.getMileage());
        car.setMaintenanceDate(request.getMaintenanceDate());
        car.setInsuranceName(request.getInsuranceName());
        car.setStatus(request.getStatus());
        car.setPurchasePrice(request.getPurchasePrice());
        car.setModelPrice(request.getModelPrice());
        car.setSeater(request.getSeater());
        car.setColor(request.getColor());
    }

    @Transactional
    public void deleteCar(Long carId) {
        Car car = carRepository.findById(carId)
                .orElseThrow(() -> new EntityNotFoundException("해당 차량이 존재하지 않습니다."));

        carRepository.delete(car);
    }
}