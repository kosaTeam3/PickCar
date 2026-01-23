package com.erp.domain.client.repository;

import com.erp.domain.client.entity.Client;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;


public interface ClientRepository extends JpaRepository<Client, Long> {

    @Query("SELECT c FROM Client c WHERE " +
            "c.name LIKE %:keyword% OR " +
            "c.email LIKE %:keyword% OR " +
            "c.phoneNumber LIKE %:keyword%")
    List<Client> searchClient(@Param("keyword") String keyword);

    // 로그인
    Optional<Client> findByEmail(String email);

    // 이메일 중복 확인
    boolean existsByEmail(String email);

    Page<Client> searchClient(@Param("keyword") String keyword, Pageable pageRequest);
}
