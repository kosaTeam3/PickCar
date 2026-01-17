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
    @Query(
            value = """
            SELECT
                DATE_FORMAT(r.start_rent_date_time, '%Y-%m') AS label,
                COUNT(r.id) AS count
            FROM rent r
            WHERE r.start_rent_date_time >= :startDate AND :endDate
              AND r.end_rent_date_time IS NOT NULL
            GROUP BY label
            ORDER BY label ASC
        """,
            nativeQuery = true
    )
    List<StatisticsProjection> findMonthlyRentCount(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate
    );

    /* 일간 대여 건수 조회 */
    @Query(
            value = """
            SELECT
                DATE_FORMAT(r.start_rent_date_time, '%Y-%m-%d') AS label,
                COUNT(r.id) AS count
            FROM rent r
            WHERE r.start_rent_date_time BETWEEN :startDate AND :endDate
              AND r.end_rent_date_time IS NOT NULL
            GROUP BY label
            ORDER BY label ASC
        """,
            nativeQuery = true
    )
    List<StatisticsProjection> findDailyRentCount(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate
    );

    /* 주간 대여 건수 조회 */
    @Query(
            value = """
            SELECT
                DATE_FORMAT(r.start_rent_date_time, '%Y-%u') AS label,
                COUNT(r.id) AS count
            FROM rent r
            WHERE r.start_rent_date_time BETWEEN :startDate AND :endDate
              AND r.end_rent_date_time IS NOT NULL
            GROUP BY label
            ORDER BY label ASC
        """,
            nativeQuery = true
    )
    List<StatisticsProjection> findWeeklyRentCount(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate
    );

    /* 인기 차종(모델별) 대여 횟수 통계 */
    @Query(value = "SELECT r.brand AS brand, r.model AS model, COUNT(r.id) AS count " +
            "FROM rent r " +
            "WHERE r.end_rent_date_time IS NOT NULL " + // 반납 완료된 건만
            "AND r.start_rent_date_time BETWEEN :startDate AND :endDate " +
            "GROUP BY r.brand, r.model " + // 차종별 그룹핑
            "ORDER BY count DESC", nativeQuery = true) // 많이 빌린 순서대로
    List<CarStatisticsProjection> findPopularCarModel(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate
    );

}
