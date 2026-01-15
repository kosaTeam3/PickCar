package com.erp.domain.maintenance.repository;

import com.erp.domain.maintenance.dto.response.MaintenanceListResponse;
import com.erp.domain.maintenance.entity.Maintenance;
import com.erp.domain.maintenance.entity.MaintenanceStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;


public interface MaintenanceRepository extends JpaRepository<Maintenance, Long> {
    @Query("""
            select new com.erp.domain.maintenance.dto.response.MaintenanceListResponse(
                m.id,
                m.car.id,
                m.branch.id,
                m.employee.id,
                m.employeeName,
                m.title,
                m.status
            )
            from Maintenance m
            where (:branchId is null or m.branch.id = :branchId)
              and (:status is null or m.status = :status)
              and (
                    :keyword is null
                    or m.title like concat('%', :keyword, '%')
                    or m.employeeName like concat('%', :keyword, '%')
                    or m.vehicleIdNumber like concat('%', :keyword, '%')
              )
            """)
    Page<MaintenanceListResponse> search(@Param("branchId") Long branchId,
                                         @Param("status") MaintenanceStatus status,
                                         @Param("keyword") String keyword,
                                         Pageable pageable);

    /* 차량 정비 이력 조회 */
    @Query("""
            SELECT m
            FROM Maintenance m
            JOIN FETCH m.employee e
            WHERE m.car.id = :cardId
            AND m.maintenanceDate <= :now
            ORDER BY m.maintenanceDate DESC
    """)
    Page<Maintenance> findMaintenanceHistory(
            @Param("cardId") Long cardId,
            @Param("now") LocalDate now,
            Pageable pageable);

}

