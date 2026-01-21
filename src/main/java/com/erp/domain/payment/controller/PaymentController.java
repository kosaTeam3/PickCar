package com.erp.domain.payment.controller;

import com.erp.domain.payment.dto.request.PaymentSaveRequest;
import com.erp.domain.payment.dto.response.PaymentSaveResponse;
import com.erp.domain.payment.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/client/payments")
@RequiredArgsConstructor
public class PaymentController {
    private final PaymentService paymentService;

    @PostMapping
    public ResponseEntity<PaymentSaveResponse> savePayment(@RequestBody PaymentSaveRequest request) {
        PaymentSaveResponse response = paymentService.savePayment(request);
        return ResponseEntity.ok(response);
    }
}
