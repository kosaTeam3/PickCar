package com.erp.domain.client.repository;

import com.erp.domain.client.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ClientRepository extends JpaRepository<Client, Long> {


    // 이메일 중복 확인
    boolean existsByEmail(String email);
}
