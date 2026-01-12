package com.erp.domain.car.dto.request;

import com.erp.domain.car.entity.CarColor;
import com.erp.domain.car.entity.CarStatus;
import com.erp.domain.car.entity.FuelType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class CarCreateRequest {

    private String vehicleIdNumber;
    private String model;
    private Long price;
    private String brand;
    private Integer year;
    private Integer ageLimit;
    private FuelType fuelType;
    private String carImage;
    private Long branchId;
    private String carNumber;
    private Long mileage;
    private LocalDate maintenanceDate;
    private String insuranceName;
    private CarStatus status;
    private Long purchasePrice;
    private Long modelPrice;
    private Integer seater;
    private CarColor color;
}
