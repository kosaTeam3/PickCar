package com.erp.domain.rent.controller;

import com.erp.domain.rent.dto.request.RentCreateRequest;
import com.erp.domain.rent.dto.response.RentCreateResponse;
import com.erp.domain.rent.service.RentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/client/rental")
@RequiredArgsConstructor
public class RentClientController {
    private final RentService rentService;

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

}
