package com.erp.domain.rent.repository;

import com.erp.domain.rent.entity.Rent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RentRepository extends JpaRepository<Rent, Long> {

    @Query("SELECT r FROM Rent r WHERE r.clientId.id = :id")
    List<Rent> findRentHistoryByClient(@Param("id") Long clientId);
}
