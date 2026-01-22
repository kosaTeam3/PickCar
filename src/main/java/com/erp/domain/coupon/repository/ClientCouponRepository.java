package com.erp.domain.coupon.repository;

import com.erp.domain.coupon.entity.ClientCoupon;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface ClientCouponRepository extends JpaRepository<ClientCoupon, Long> {

    /* 특정 유저(ClientId)가 가진 모든 쿠폰 조회 */
    List<ClientCoupon> findByClientId(Long clientId);

    /* 중복 발급 확인 메서드 */
    boolean existsByClientIdAndCouponId(Long clientId, Long couponId);

    /* 쿠폰 사용 현황 조회 */
    long countByCouponIdAndIsUsedTrue(Long couponId);

    // 쿠폰 사용 처리 (원자적 업데이트) - 동시성 제어용
    @Modifying(clearAutomatically = true)
    @Query("UPDATE ClientCoupon c SET c.isUsed = true, c.usedAt = :usedAt WHERE c.id = :id AND c.isUsed = false")
    int updateStatusToUsed(@Param("id") Long id, @Param("usedAt") LocalDateTime usedAt);

    /* 특정 쿠폰 발급 내역 조회 */
    Page<ClientCoupon> findByCouponId(Long couponId, Pageable pageable);
}
