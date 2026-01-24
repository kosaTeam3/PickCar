package com.erp.domain.rent.repository;

import com.erp.domain.rent.entity.RentStatus;

import java.time.LocalDateTime;

public interface RentHistoriesProjection {

    Long getCarId();
    String getStringImage();
    String getModel();
    String getBrand();
    Integer getYear();
    RentStatus getStatus();
    Long getBranchId();
    LocalDateTime getStartRentDateTime();
    LocalDateTime getEndRentDateTime();
}

