package com.erp.domain.car.dto.request;

import com.erp.domain.car.entity.CarStatus;
import com.erp.domain.car.entity.FuelType;

public record CarSearchRequest (

    Long branchId,
    String brand,
    String model,
    FuelType fuelType,
    CarStatus status,
    String carNumber

) {
}

