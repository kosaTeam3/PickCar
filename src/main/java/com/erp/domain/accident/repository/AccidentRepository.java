package com.erp.domain.accident.repository;

import com.erp.domain.accident.entity.Accident;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

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
            """,
            countQuery = "SELECT count(a) FROM Accident a")
    Page<Accident> findAllWithDetails(Pageable pageable);
}
