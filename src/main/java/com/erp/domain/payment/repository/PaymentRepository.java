package com.erp.domain.payment.repository;

import com.erp.domain.payment.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    boolean existsByImpUid(String impUid);

    Optional<Payment> findByRentId(Long rentId);
}
