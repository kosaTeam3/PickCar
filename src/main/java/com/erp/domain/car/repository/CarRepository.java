package com.erp.domain.car.repository;

import com.erp.domain.car.entity.Car;
import com.erp.domain.car.entity.CarStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CarRepository extends JpaRepository<Car, Long> {
    // 특정 지점의 WAITING 상태 차량 전체 카운트
    int countByBranchIdAndStatus(Long branchId, CarStatus status);

    // 특정 지점의 WAITING 상태 차량 중, 예약된 차량들을 제외하고 카운트
    @Query("""
        SELECT COUNT(c) FROM Car c
        WHERE c.branch.id = :branchId
        AND c.status = :status
        AND c.id NOT IN :rentedCarIds""")
    int countAvailableCarsNotIn(@Param("branchId") Long branchId,
                                @Param("status") CarStatus status,
                                @Param("rentedCarIds") List<Long> rentedCarIds);

    // 특정 지점의 가용 차량 목록 조회 (예약 차량 제외)
    @Query("""
        SELECT c FROM Car c
        WHERE c.branch.id = :branchId
        AND c.status = :status
        AND (:rentedCarIds IS NULL OR c.id NOT IN :rentedCarIds)""")
    List<Car> findAvailableCars(@Param("branchId") Long branchId,
                                @Param("status") CarStatus status,
                                @Param("rentedCarIds") List<Long> rentedCarIds);
}
