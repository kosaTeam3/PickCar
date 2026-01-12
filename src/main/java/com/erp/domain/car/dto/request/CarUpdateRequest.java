package com.erp.domain.car.dto.request;

import com.erp.domain.car.entity.CarColor;
import com.erp.domain.car.entity.CarStatus;
import com.erp.domain.car.entity.FuelType;

import java.time.LocalDate;

public record CarUpdateRequest(

        String vehicleIdNumber,
        String model,
        Long price,
        String brand,
        Integer year,
        Integer ageLimit,
        FuelType fuelType,
        String carImage,
        Long branchId,
        String carNumber,
        Long mileage,
        LocalDate maintenanceDate,
        String insuranceName,
        CarStatus status,
        Long purchasePrice,
        Long modelPrice,
        Integer seater,
        CarColor color
) {
}
