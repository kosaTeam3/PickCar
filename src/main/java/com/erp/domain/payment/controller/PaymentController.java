package com.erp.domain.payment.controller;

import com.erp.domain.payment.dto.request.PaymentSaveRequest;
import com.erp.domain.payment.dto.response.PaymentSaveResponse;
import com.erp.domain.payment.service.PaymentService;
import com.erp.domain.payment.service.PortOnePaymentInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/client/payments")
@RequiredArgsConstructor
public class PaymentController {
    private final PaymentService paymentService;

    @PostMapping
    public ResponseEntity<PaymentSaveResponse> savePayment(@RequestBody PaymentSaveRequest request) {
        PortOnePaymentInfo paymentInfo = paymentService.getPortOnePaymentInfo(request.impUid());
        // Transactional 비관적 락 적용
        PaymentSaveResponse response = paymentService.savePayment(request, paymentInfo);

        return ResponseEntity.ok(response);
    }
}
