package com.erp.domain.accident.repository;

import com.erp.domain.accident.entity.Accident;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AccidentRepository extends JpaRepository<Accident, Long> {
    // todo
//    @Query("SELECT a FROM Accident a WHERE a.client.id = :clientId")
//    List<Accident> findByClientId(Long clientId);
}
