package com.erp.domain.car.repository;

import com.erp.domain.car.entity.Car;
import com.erp.domain.car.entity.CarStatus;
import com.erp.domain.car.entity.FuelType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CarRepository extends JpaRepository<Car, Long> {
    // 특정 지점의 WAITING 상태 차량 전체 카운트
    int countByBranchIdAndStatus(Long branchId, CarStatus status);

    // 특정 지점의 WAITING 상태 차량 중, 예약된 차량들을 제외하고 카운트
    @Query("SELECT COUNT(c) FROM Car c " +
            "WHERE c.branch.id = :branchId " +
            "AND c.status = :status " +
            "AND c.id NOT IN :rentedCarIds")
    int countAvailableCarsNotIn(@Param("branchId") Long branchId,
                                @Param("status") CarStatus status,
                                @Param("rentedCarIds") List<Long> rentedCarIds);


    /* 차량 검색 */
    @Query("SELECT c FROM Car c " +
            "WHERE (:branchId IS NULL OR c.branch.id = :branchId) " +
            "AND (:brand IS NULL OR c.brand = :brand) " +
            "AND (:model IS NULL OR c.model LIKE %:model%) " +
            "AND (:fuelType IS NULL OR c.fuelType = :fuelType) " +
            "AND (:status IS NULL OR c.status = :status)")
    Page<Car> searchCars(
            @Param("branchId") Long branchId,
            @Param("brand") String brand,
            @Param("model") String model,
            @Param("fuelType") FuelType fuelType,
            @Param("status") CarStatus status,
            Pageable pageable);
}
