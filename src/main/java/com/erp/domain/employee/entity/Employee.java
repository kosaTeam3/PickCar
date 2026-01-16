package com.erp.domain.employee.entity;

import com.erp.common.entity.BaseTimeEntity;
import com.erp.domain.branch.entity.Branch;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDate;

@Entity
@Table(name = "employee")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Employee extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "branch_id", nullable = false)
    private Branch branch;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "phone_number", nullable = false)
    private String phoneNumber;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "grade", nullable = false)
    private String grade;

    @Enumerated(EnumType.STRING)
    @Column(name = "authority", nullable = false)
    private EmployeeAuthority authority;

    @Column(name = "entry_date", nullable = false)
    private LocalDate entryDate;

    @Column(name = "quit_date")
    private LocalDate quitDate;

    @Column(name = "login_id", nullable = false)
    private String loginId;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "join_change_password", nullable = false, columnDefinition = "TINYINT(1)")
    @ColumnDefault("1")
    @Builder.Default
    private Boolean passwordChangeRequired = true;

    // Soft Delete
    public void resign(LocalDate date) {
        this.quitDate = date;
    }
}