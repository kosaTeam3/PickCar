package com.erp.domain.branch.entity;

import com.erp.common.entity.BaseTimeEntity;
import com.erp.domain.employee.entity.Employee;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "branch")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Branch extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "phone_number", nullable = false)
    private String phoneNumber;

    @Column(name = "address", nullable = false)
    private String address;

    @Column(name = "latitude", nullable = false)
    private Double latitude;

    @Column(name = "longitude", nullable = false)
    private Double longitude;

    @Column(name = "employ_count", nullable = false)
    private Integer employeeCount;

    @Column(name = "car_count", nullable = false)
    private Integer carCount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "manager_id")
    private Employee manager;

    @Column(name = "manager_name")
    private String managerName;
}
