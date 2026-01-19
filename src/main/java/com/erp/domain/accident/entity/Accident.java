package com.erp.domain.accident.entity;

import com.erp.common.entity.BaseTimeEntity;
import com.erp.domain.car.entity.Car;
import com.erp.domain.client.entity.Client;
import com.erp.domain.rent.entity.Rent;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "accident")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Accident extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rent_id")
    private Rent rent;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "car_id", nullable = false)
    private Car car;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private AccidentStatus status; // 상태

    @Column(name = "description")
    private String description; // 사고 설명

    @Column(name = "location")
    private String location; // 대략적인 사고 위치

    @Column(name = "time", nullable = false)
    private LocalDateTime time; // 사고 시각

    @Column(name = "part", nullable = false)
    private String part; // 사고 부위

    @Column(name = "repair_cost")
    private Long repairCost; // 수리비

    @Column(name = "client_liability")
    private Long clientLiability; // 고객 부담금
}
