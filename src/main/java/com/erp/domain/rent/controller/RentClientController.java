package com.erp.domain.rent.controller;

import com.erp.domain.rent.dto.request.RentCreateRequest;
import com.erp.domain.rent.dto.response.CurrentRentResponse;
import com.erp.domain.rent.dto.response.RentCreateResponse;
import com.erp.domain.rent.service.RentService;
import com.sun.security.auth.UserPrincipal;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/client/rental")
@RequiredArgsConstructor
public class RentClientController {
    private final RentService rentService;

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


}
