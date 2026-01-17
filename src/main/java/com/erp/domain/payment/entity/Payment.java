package com.erp.domain.payment.entity;

import com.erp.common.entity.BaseTimeEntity;
import com.erp.domain.rent.entity.Rent;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "payment")
@Getter
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

    @Column(name = "imp_uid", nullable = false) // 포트원 결제 고유 번호
    private String impUid;

    @Column(name = "merchant_uid", nullable = false) // 우리가 생성한 주문 번호
    private String merchantUid;

    @Column(name = "amount", nullable = false)
    private Long amount;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private PaymentStatus status; // READY, PAID, FAILED, CANCELLED

    @Column(name = "payment_method")
    private String paymentMethod; // CARD, KAKAOPAY, etc.
}
