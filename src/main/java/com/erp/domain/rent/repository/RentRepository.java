package com.erp.domain.rent.repository;

import com.erp.domain.car.entity.Car;
import com.erp.domain.rent.entity.Rent;
import com.erp.domain.rent.entity.RentStatus;
import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
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

    @Query("""
            SELECT r
            FROM Rent r
            JOIN FETCH r.car c
            WHERE r.client.id = :id
            ORDER BY r.startRentDateTime DESC
            """)
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

    // 비관적 락을 사용하여 예약을 조회
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT r FROM Rent r WHERE r.id = :id")
    Optional<Rent> findByIdWithLock(@Param("id") Long id);

    // 고객의 현재 렌트중인(RESERVED 상태 + 현재 시간이 대여 기간에 포함됨) 차량 정보 조회
    @Query("""
            SELECT r
            FROM Rent r
            JOIN FETCH r.car c
            JOIN FETCH c.branch b
            WHERE r.client.id = :clientId
            AND :now BETWEEN r.startRentDateTime AND r.endRentDateTime
            AND r.status = :status
            """)
    Optional<Rent> findCurrentRentByClient(
            @Param("clientId") Long clientId,
            @Param("now") LocalDateTime now,
            @Param("status") RentStatus status);

    @Query("""
                select
                    c.id as carId,
                    c.image as stringImage,
                    c.model as model,
                    c.brand as brand,
                    c.year as year,
                    r.status as status,
                    b.id as branchId,
                    r.startRentDateTime as startRentDateTime,
                    r.endRentDateTime as endRentDateTime
                from Rent r
                join r.car c
                join c.branch b
                where r.client.id = :clientId
            """)
    Page<RentHistoriesProjection> findRentHistories(@Param("clientId") Long clientId, Pageable pageable);
}
