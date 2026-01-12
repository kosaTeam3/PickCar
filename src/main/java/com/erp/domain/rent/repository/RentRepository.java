package com.erp.domain.rent.repository;

import com.erp.domain.rent.entity.Rent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface RentRepository extends JpaRepository<Rent, Long> {
    List<Rent> findByClientId_Id(Long clientId);
}
