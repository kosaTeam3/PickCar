package com.erp.domain.coupon.entity;

import com.erp.common.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "coupon")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Coupon extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "coupon_name", nullable = false)
    private String couponName;

    @Column(name = "discount", nullable = false)
    private Integer discount;

    @Column(name = "exp_date", nullable = false)
    private LocalDate expDate;

    @Column(name = "code", unique = true)
    private String code;

    @Column(name = "min_price")
    private Integer minPrice;
}

