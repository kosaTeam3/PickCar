package com.erp.domain.car.entity;

import com.erp.common.entity.BaseTimeEntity;
import com.erp.domain.branch.entity.Branch;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "car")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Car extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "branch_id", nullable = false)
    private Branch branch;

    @Column(name = "vehicle_id_number", nullable = false)
    private String vehicleIdNumber;

    @Column(name = "model", nullable = false)
    private String model;

    @Column(name = "price", nullable = false)
    private Long price;

    @Column(name = "brand", nullable = false)
    private String brand;

    @Column(name = "year", nullable = false)
    private Integer year;

    @Column(name = "age_limit", nullable = false)
    private Integer ageLimit;

    @Enumerated(EnumType.STRING)
    @Column(name = "fuel_type", nullable = false)
    private FuelType fuelType;

    @Column(name = "image")
    private String image;

    @Column(name = "car_number", nullable = false)
    private String carNumber;

    @Column(name = "mileage", nullable = false)
    private Long mileage;

    @Column(name = "maintenance_date")
    private LocalDate maintenanceDate;

    @Column(name = "insurance_name")
    private String insuranceName;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private CarStatus status;

    @Column(name = "purchase_price", nullable = false)
    private Long purchasePrice;

    @Column(name = "model_price", nullable = false)
    private Long modelPrice;

    @Column(name = "seater", nullable = false)
    private Integer seater;

    @Enumerated(EnumType.STRING)
    @Column(name = "color", nullable = false)
    private CarColor color;

    @Column(name = "last_maintenance_mileage", nullable = false, columnDefinition = "bigint default 0")
    @Builder.Default
    private Long lastMaintenanceMileage = 0L;
}
