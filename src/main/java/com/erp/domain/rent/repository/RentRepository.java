package com.erp.domain.rent.repository;

import com.erp.domain.rent.entity.Rent;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface RentRepository extends JpaRepository<Rent, Long> {
    // 요청 기간 (start ~ end)에 겹치는 예약이 있는 car_id 목록 조회
    @Query("SELECT r.car.id FROM Rent r " +
            "WHERE r.startRentDateTime < :endRentDateTime AND r.endRentDateTime > :startRentDateTime")
    List<Long> findRentedCarIds(@Param("startRentDateTime") LocalDateTime startRentDateTime,
                                @Param("endRentDateTime") LocalDateTime endRentDateTime);

    @Query("SELECT r FROM Rent r WHERE r.client.id = :id")
    List<Rent> findRentHistoryByClient(@Param("id") Long clientId);

    /* 차량 대여 이력 조회 */
    @Query("""
            SELECT r
            FROM Rent r
            JOIN FETCH r.client c
            WHERE r.car.id = :carId
            AND r.endRentDateTime < :now
            ORDER BY r.startRentDateTime DESC
    """)
    Page<Rent> findRentHistoryByCarId(
            @Param("carId") Long carId,
            @Param("now") LocalDateTime now,
            Pageable pageable);
}
