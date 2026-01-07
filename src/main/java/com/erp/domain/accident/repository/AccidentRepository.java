package com.erp.domain.accident.repository;

import com.erp.domain.accident.entity.Accident;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccidentRepository extends JpaRepository<Accident, Long> {
}
