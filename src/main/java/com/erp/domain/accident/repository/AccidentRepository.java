package com.erp.domain.accident.repository;

import com.erp.domain.accident.entity.Accident;
import com.erp.domain.accident.entity.AccidentStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;

public interface AccidentRepository extends JpaRepository<Accident, Long> {
    // todo
//    @Query("SELECT a FROM Accident a WHERE a.client.id = :clientId")
//    List<Accident> findByClientId(Long clientId);

    @Query(value = """
            SELECT a
            FROM Accident a
            JOIN FETCH a.car c
            LEFT JOIN FETCH a.rent r
            LEFT JOIN FETCH r.client cl
            WHERE (:startAt IS NULL OR a.time >= :startAt)
              AND (:endAt IS NULL OR a.time <= :endAt)
              AND (:vehicleIdNumber IS NULL OR c.vehicleIdNumber LIKE %:vehicleIdNumber%)
              AND (:clientName IS NULL OR cl.name LIKE %:clientName%)
              AND (:status IS NULL OR a.status = :status)
            """,
            countQuery = """
            SELECT count(a)
            FROM Accident a
            LEFT JOIN a.rent r
            LEFT JOIN r.client cl
            WHERE (:startAt IS NULL OR a.time >= :startAt)
              AND (:endAt IS NULL OR a.time <= :endAt)
              AND (:vehicleIdNumber IS NULL OR a.car.vehicleIdNumber LIKE %:vehicleIdNumber%)
              AND (:clientName IS NULL OR cl.name LIKE %:clientName%)
              AND (:status IS NULL OR a.status = :status)
            """)
    Page<Accident> findAllWithFilters(
            @Param("startAt") LocalDateTime startAt,
            @Param("endAt") LocalDateTime endAt,
            @Param("vehicleIdNumber") String vehicleIdNumber,
            @Param("clientName") String clientName,
            @Param("status") AccidentStatus status,
            Pageable pageable);
}
