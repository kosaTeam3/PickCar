package com.erp.domain.coupon.repository;

import com.erp.domain.coupon.entity.ClientCoupon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface ClientCouponRepository extends JpaRepository<ClientCoupon, Long> {

    /* 특정 유저(ClientId)가 가진 모든 쿠폰 조회 */
    List<ClientCoupon> findByClientId(Long clientId);

    // 쿠폰 사용 처리 (원자적 업데이트) - 동시성 제어용
    @Modifying(clearAutomatically = true)
    @Query("UPDATE ClientCoupon c SET c.isUsed = true, c.usedAt = :usedAt WHERE c.id = :id AND c.isUsed = false")
    int updateStatusToUsed(@Param("id") Long id, @Param("usedAt") LocalDateTime usedAt);
}
