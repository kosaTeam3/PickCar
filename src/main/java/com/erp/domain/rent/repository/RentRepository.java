package com.erp.domain.rent.repository;

import com.erp.domain.rent.entity.Rent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RentRepository extends JpaRepository<Rent, Long> {
}
