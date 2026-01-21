package com.erp.domain.statistics.repository;

import com.erp.domain.rent.entity.Rent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface StatisticsRepository extends JpaRepository<Rent, Long> {

    /* 월간 대여 건수 조회 */
    @Query(value = """
            SELECT
                DATE_FORMAT(r.start_rent_date_time, '%Y-%m') AS label,
                COUNT(r.id) AS count
            FROM rent r
            WHERE r.start_rent_date_time >= :startDate AND :endDate
              AND r.end_rent_date_time IS NOT NULL
            GROUP BY label
            ORDER BY label ASC
        """,
            nativeQuery = true)
    List<StatisticsProjection> findMonthlyRentCount(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate
    );

    /* 일간 대여 건수 조회 */
    @Query(value = """
            SELECT
                DATE_FORMAT(r.start_rent_date_time, '%Y-%m-%d') AS label,
                COUNT(r.id) AS count
            FROM rent r
            WHERE r.start_rent_date_time BETWEEN :startDate AND :endDate
              AND r.end_rent_date_time IS NOT NULL
            GROUP BY label
            ORDER BY label ASC
        """,
            nativeQuery = true)
    List<StatisticsProjection> findDailyRentCount(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate
    );

    /* 주간 대여 건수 조회 */
    @Query(value = """
            SELECT
                DATE_FORMAT(r.start_rent_date_time, '%Y-%u') AS label,
                COUNT(r.id) AS count
            FROM rent r
            WHERE r.start_rent_date_time BETWEEN :startDate AND :endDate
              AND r.end_rent_date_time IS NOT NULL
            GROUP BY label
            ORDER BY label ASC
        """,
            nativeQuery = true)
    List<StatisticsProjection> findWeeklyRentCount(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate
    );

    /* 인기 차종(모델별) 대여 횟수 통계 */
    @Query(value = """
            SELECT
                c.brand AS brand,
                c.model AS model,
                COUNT(r.id) AS count
            FROM rent r
            INNER JOIN car c ON r.car_id = c.id
            WHERE r.start_rent_date_time BETWEEN :startDate AND :endDate
              AND r.status != 'CANCELLED'
            GROUP BY c.brand, c.model
            ORDER BY count DESC
            """,
            nativeQuery = true)
    List<CarStatisticsProjection> findPopularCarModel(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate
    );

    /* 월간 매출 통계 */
    @Query(value = """
        SELECT 
            DATE_FORMAT(p.paid_at, '%Y-%m') As label,
            SUM(p.amount) AS count
        FROM payment p
        WHERE p.paid_at BETWEEN :startDate AND :endDate
            AND p.status = 'PAID'
        GROUP BY label
        ORDER BY label ASC 
    """,
        nativeQuery = true)
    List<StatisticsProjection> findMonthlySales(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate
    );

    /* 주간 매출 통계 */
    @Query(value = """
    SELECT 
        DATE_FORMAT(DATE_SUB(p.paid_at, INTERVAL (WEEKDAY(p.paid_at)) DAY), '%Y-%m-%d') AS label,
        SUM(p.amount) AS count
    FROM payment p
    WHERE p.paid_at BETWEEN :startDate AND :endDate
      AND p.status = 'PAID'
    GROUP BY label
    ORDER BY label ASC
    """,
        nativeQuery = true)
    List<StatisticsProjection> findWeeklySales(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate
    );

    /* 일간 매출 통계 */
    @Query(value = """
    SELECT 
        DATE_FORMAT(p.paid_at, '%Y-%m-%d') AS label,
        SUM(p.amount) AS count
    FROM payment p
    WHERE p.paid_at BETWEEN :startDate AND :endDate
      AND p.status = 'PAID'
    GROUP BY label
    ORDER BY label ASC
    """,
        nativeQuery = true)
    List<StatisticsProjection> findDailySales(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);


}
