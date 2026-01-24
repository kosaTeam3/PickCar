package com.erp.domain.rent.controller;

import com.erp.domain.payment.service.PaymentService;
import com.erp.domain.rent.dto.request.RentCancelRequest;
import com.erp.domain.rent.dto.request.RentCreateRequest;
import com.erp.domain.rent.dto.response.CurrentRentResponse;
import com.erp.domain.rent.dto.response.RentCancelResponse;
import com.erp.domain.rent.dto.response.RentCreateResponse;
import com.erp.domain.rent.dto.response.RentHistListResponse;
import com.erp.domain.rent.service.RentService;
import com.sun.security.auth.UserPrincipal;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/client/rentals")
@PreAuthorize("hasAnyRole('CLIENT')")
@RequiredArgsConstructor
public class RentClientController {
    private final RentService rentService;
    private final PaymentService paymentService;

    @PostMapping
    public ResponseEntity<RentCreateResponse> createRent(
            @Valid @RequestBody RentCreateRequest request,
            @AuthenticationPrincipal UserPrincipal user
    ) {
        Long userId = Long.parseLong(user.getName());

        RentCreateResponse response = rentService.createRent(request, userId);

        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<CurrentRentResponse> getCurrentRent(@AuthenticationPrincipal UserPrincipal user) {
        Long userId = Long.parseLong(user.getName());

        CurrentRentResponse response = rentService.findCurrentRent(userId);

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

    @GetMapping("/history")
    public ResponseEntity<Page<RentHistListResponse>> getRentHistories(
            @PageableDefault(sort = "id", direction = Sort.Direction.DESC) Pageable pageRequest,
            @AuthenticationPrincipal UserPrincipal user) {
        return ResponseEntity.ok(rentService.getRentHistories(Long.parseLong(user.getName()), pageRequest));
    }
}
