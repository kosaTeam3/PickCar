package com.erp.domain.rent.entity;

import com.erp.common.entity.BaseTimeEntity;
import com.erp.domain.car.entity.Car;
import com.erp.domain.car.entity.FuelType;
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

    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client clientId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "car_id", nullable = false)
    private Car car;

    @Column(name = "car_name", nullable = false)
    private String carName;

    @Column(name = "car_image")
    private String carImage;

    @Column(name = "model", nullable = false)
    private String model;

    @Column(name = "price", nullable = false)
    private String price;

    @Column(name = "brand", nullable = false)
    private String brand;

    @Column(name = "year", nullable = false)
    private Integer year;

    @Column(name = "start_rent_date_time")
    private LocalDateTime startRentDateTime;

    @Column(name = "end_rent_date_time")
    private LocalDateTime endRentDateTime;

    @Enumerated(EnumType.STRING)
    @Column(name = "fuel_type", nullable = false)
    private FuelType fuelType;

    @Column(name = "age_limit", nullable = false)
    private String ageLimit;

    @Column(name = "seater", nullable = false)
    private String seater;

    @Column(name = "color", nullable = false)
    private String color;
}
