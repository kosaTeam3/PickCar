package com.erp.domain.coupon.repository;

import com.erp.domain.coupon.entity.ClientCoupon;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ClientCouponRepository extends JpaRepository<ClientCoupon, Long> {

    /* 특정 유저(ClientId)가 가진 모든 쿠폰 조회 */
    List<ClientCoupon> findByClientId(Long clientId);
}
