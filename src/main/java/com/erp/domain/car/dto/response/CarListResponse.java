package com.erp.domain.car.dto.response;

import lombok.Builder;

@Builder
public record CarListResponse (

    Long carId,
    String vehicleIdNumber,
    String image,
    String model,
    Long branchId,
    String branchName,
    String carNumber,
    Integer ageLimit,
    Long mileage,
    String status,
    String fuelType,
    Integer seater,
    String color

) {
}
