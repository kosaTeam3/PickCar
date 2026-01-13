package com.erp.domain.car.dto.response;

import lombok.Builder;

import java.time.LocalDate;

@Builder
public record CarDetailResponse(

        Long carId,
        String vehicleIdNumber,
        String model,
        Long price,
        String brand,
        Integer year,
        Integer ageLimit,
        String fuelType,
        String image,
        Long branchId,
        String carNumber,
        Long mileage,
        LocalDate maintenanceDate,
        String insuranceName,
        String status,
        Long purchasePrice,
        Long modelPrice,
        Integer seater,
        String color

) {
}
