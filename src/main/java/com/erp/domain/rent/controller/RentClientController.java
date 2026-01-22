package com.erp.domain.rent.controller;

import com.erp.domain.payment.service.PaymentService;
import com.erp.domain.rent.dto.request.RentCancelRequest;
import com.erp.domain.rent.dto.request.RentCreateRequest;
import com.erp.domain.rent.dto.response.CurrentRentResponse;
import com.erp.domain.rent.dto.response.RentCancelResponse;
import com.erp.domain.rent.dto.response.RentCreateResponse;
import com.erp.domain.rent.service.RentService;
import com.sun.security.auth.UserPrincipal;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/client/rentals")
@RequiredArgsConstructor
public class RentClientController {
    private final RentService rentService;
    private final PaymentService paymentService;

    @PostMapping
    public ResponseEntity<RentCreateResponse> createRent(
            @Valid @RequestBody RentCreateRequest request
            // @AuthenticationPrincipal UserDetails userDetails // 인증된 사용자 정보 주입
            ) {
        // Long clientId = Long.parseLong(userDetails.getUsername());
        Long clientId = 1L; // DB에 있는 테스트용 고객 ID, 로그인 구현 후 주석 복원 필요

        RentCreateResponse response = rentService.createRent(request, clientId);

        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<CurrentRentResponse> getCurrentRent() {
        // Long clientId = Long.parseLong(userDetails.getUsername());
        Long clientId = 1L; // DB에 있는 테스트용 고객 ID, 로그인 구현 후 주석 복원 필요

        CurrentRentResponse response = rentService.findCurrentRent(clientId);

        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{rentId}")
    public ResponseEntity<RentCancelResponse> cancelPayment(
            @PathVariable Long rentId,
            @Valid @RequestBody RentCancelRequest request,
            @AuthenticationPrincipal UserPrincipal user
    ) {
        Long userId = Long.parseLong(user.getName());

        RentCancelResponse response = paymentService.cancelPayment(rentId, request.reason(), userId);

        return ResponseEntity.ok(response);
    }
}
