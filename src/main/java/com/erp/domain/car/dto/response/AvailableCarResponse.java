package com.erp.domain.car.dto.response;

import com.erp.domain.car.entity.CarColor;
import com.erp.domain.car.entity.FuelType;

public record AvailableCarResponse(
        Long carId,
        String carImage,
        String model,
        Long rentalFee, // 총 렌트 요금
        String brand,
        Integer year,
        Integer ageLimit,
        FuelType fuelType,
        Integer seater,
        CarColor color
) {
}
