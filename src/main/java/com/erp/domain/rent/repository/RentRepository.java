package com.erp.domain.rent.repository;

import com.erp.domain.car.entity.Car;
import com.erp.domain.rent.entity.Rent;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface RentRepository extends JpaRepository<Rent, Long> {
    // 요청 기간 (start ~ end)에 겹치는 예약이 있는 car_id 목록 조회
    @Query("""
        SELECT r.car.id FROM Rent r
        WHERE r.startRentDateTime < :endRentDateTime
            AND r.endRentDateTime > :startRentDateTime""")
    List<Long> findRentedCarIds(@Param("startRentDateTime") LocalDateTime startRentDateTime,
                                @Param("endRentDateTime") LocalDateTime endRentDateTime);

    // 해당 시간에 해당 차량을 점유하고 있는 예약 정보 조회
    @Query("""
        SELECT r FROM Rent r
        WHERE r.car.id = :carId
            AND :now BETWEEN r.startRentDateTime AND r.endRentDateTime""")
    Optional<Rent> findCurrentRent(@Param("carId") Long carId, @Param("now") LocalDateTime now);

    @Query("SELECT r FROM Rent r WHERE r.client.id = :id")
    List<Rent> findRentHistoryByClient(@Param("id") Long clientId);

    Long car(Car car);

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

    // 요청 기간 내 'WAITING_PAYMENT', 'RESERVED' 상태인 예약이 존재하는지 확인
    // (겹치는 기간: 요청 시작 < 기존 종료 && 요청 종료 > 기존 시작)
    @Query("""
        SELECT COUNT(r) > 0 FROM Rent r
        WHERE r.car.id = :carId
        AND r.status IN ('WAITING_PAYMENT', 'RESERVED')
        AND r.startRentDateTime < :endDateTime
        AND r.endRentDateTime > :startDateTime
    """)
    boolean existsOverlappingRent(@Param("carId") Long carId,
                                  @Param("startDateTime") LocalDateTime startDateTime,
                                  @Param("endDateTime") LocalDateTime endDateTime);
}
