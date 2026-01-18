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
    private Long price; // 1시간당 요금

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

    // 중소형 업체 특성상 일괄 계약이므로 기본값을 지정하거나 상수로 관리
    @Column(name = "insurance_name", columnDefinition = "varchar(255) default '전국렌터카공제조합'")
    @Builder.Default
    private String insuranceName = "전국렌터카공제조합";

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

    @Transient
    private static final int DAILY_CAP_HOUR_THRESHOLD = 10; // 일일 상한 시간 (10시간)

    // 최종 렌트 요금을 계산하는 로직
    public Long calculateRentalFee(long totalHours) {
        long days = totalHours / 24;
        long remainingHours = totalHours % 24;
        Long dailyCapFee = this.getDailyCapFee(); // 일일 상한 요금(1일당 요금)
        long totalFee = 0L;

        if (days > 0) {
            totalFee += days * dailyCapFee;
        }

        // 24시간 단위로 나누어 떨어지지 않는 남은 시간에 대한 요금 계산
        long hourlySum = remainingHours * this.price;

        totalFee += Math.min(hourlySum, dailyCapFee);

        return totalFee;
    }

    public Long getDailyCapFee() { // 일일 상한 요금 계산
        if (this.price == null) {
            return 0L;
        }

        return this.price * DAILY_CAP_HOUR_THRESHOLD;
    }
}
