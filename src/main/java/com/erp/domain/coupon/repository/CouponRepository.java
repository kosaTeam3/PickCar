package com.erp.domain.coupon.repository;

import com.erp.domain.coupon.entity.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CouponRepository extends JpaRepository<Coupon, Long> {

    /* 쿠폰 코드로 조회 */
    Optional<Coupon> findAllByCode(String couponCode);
}
