package com.erp.domain.coupon.repository;

import com.erp.domain.coupon.entity.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface CouponRepository extends JpaRepository<Coupon, Long> {

    /* 쿠폰 코드로 조회 */
    Optional<Coupon> findAllByCode(String couponCode);

    /* 쿠폰 목록(최신순) 전체 조회 */
    List<Coupon> findAllByOrderByIdDesc();

    /* 이벤트 배너용 쿠폰 목록 조회(오늘 날짜 기준으로 발급 가능한 쿠폰 목록)  */
    @Query("""
            SELECT c
            FROM Coupon c
            WHERE c.startDate <= :today
            AND c.endDate >= :today
            AND c.issuedQuantity < c.maxQuantity
            ORDER BY c.id DESC
            """)
    List<Coupon> findActiveCoupons(@Param("today") LocalDate today);
}
