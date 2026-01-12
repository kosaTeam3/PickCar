package com.erp.domain.client.entity;

import com.erp.common.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "client")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Client extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "birthday", nullable = false)
    private LocalDate birthday;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender", nullable = false)
    private Gender gender;

    @Column(name = "licence_number", nullable = false)
    private String licenceNumber;

    @Column(name = "licence_area", nullable = false)
    private String licenceArea;

    @Column(name = "licence_day", nullable = false)
    private LocalDate licenceDay;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "blacklisted", nullable = false)
    @Builder.Default
    private Boolean blacklisted = false;

    @Column(name = "blacklist_info")
    private String blacklistInfo;

    @Column(name = "phone_number", nullable = false)
    private String phoneNumber;
}

