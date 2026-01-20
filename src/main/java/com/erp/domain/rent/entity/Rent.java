package com.erp.domain.rent.entity;

import com.erp.common.entity.BaseTimeEntity;
import com.erp.domain.car.entity.Car;
import com.erp.domain.client.entity.Client;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "rent")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Rent extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "car_id", nullable = false)
    private Car car;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private RentStatus status;

    @Column(name = "rental_fee", nullable = false)
    private Long rentalFee;

    @Column(name = "start_rent_date_time")
    private LocalDateTime startRentDateTime;

    @Column(name = "end_rent_date_time")
    private LocalDateTime endRentDateTime;
}
