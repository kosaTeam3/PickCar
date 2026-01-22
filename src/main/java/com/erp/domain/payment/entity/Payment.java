package com.erp.domain.payment.entity;

import com.erp.common.entity.BaseTimeEntity;
import com.erp.domain.client.entity.Client;
import com.erp.domain.rent.entity.Rent;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "payment")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Payment extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rent_id", nullable = false)
    private Rent rent;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    @Column(name = "imp_uid", nullable = false, unique = true)
    private String impUid; // 포트원 결제 고유 번호

    @Column(name = "merchant_uid", nullable = false)
    private String merchantUid; // 생성한 주문 번호

    @Column(name = "amount", nullable = false)
    private Long amount; // 사용자가 실제로 결제한 최종 금액

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private PaymentStatus status; // READY, PAID, FAILED, CANCELLED

    @Column(name = "pg_provider")
    private String pgProvider; // PG사 정보: html5_inicis, kakaopay

    @Column(name = "payment_method")
    private String paymentMethod; // card

    @Column(name = "paid_at")
    private LocalDateTime paidAt; // 실제 결제 완료 시각
}
