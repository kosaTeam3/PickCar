package com.erp.domain.client.controller;

import com.erp.domain.client.dto.request.*;
import com.erp.domain.client.dto.response.ClientInfoResponse;
import com.erp.domain.client.dto.response.MypageResponse;
import com.erp.domain.client.service.ClientService;
import com.erp.global.jwt.TokenInfo;
import com.sun.security.auth.UserPrincipal;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/client")
@RequiredArgsConstructor
public class ClientController {

    private final ClientService clientService;

    @PreAuthorize("hasAnyRole('CLIENT')")
    @GetMapping
    public ResponseEntity<ClientInfoResponse> getInfo(@AuthenticationPrincipal UserPrincipal user) {
        return ResponseEntity.ok(clientService.getInfo(Long.parseLong(user.getName())));
    }

    // 이메일 중복 확인
    @PostMapping("/validation")
    public ResponseEntity<String> checkEmail(@RequestBody EmailCheckRequestDto requestDto) {

        clientService.checkEmailDuplicate(requestDto.email());
        return ResponseEntity.ok().build();
    }

    // 로그인 페이지
    @PostMapping("/login")
    public TokenInfo login(@RequestBody LoginRequestDto loginRequestDto) {
        return clientService.login(loginRequestDto);
    }

    // 회원가입
    @PostMapping("/register")
    public ResponseEntity<Void> registerClient(@Valid @RequestBody RegisterClientRequestDto requestDto) {

        clientService.registerClient(requestDto);
        return ResponseEntity.noContent().build();
    }

    // 내정보 조회
    @GetMapping("/mypage")
    @PreAuthorize("hasAnyRole('CLIENT')")
    public ResponseEntity<MypageResponse> getMypage(
            @AuthenticationPrincipal UserPrincipal user
    ) {
        return ResponseEntity.ok(clientService.getMypage(Long.parseLong(user.getName())));
    }

    @PatchMapping("/mypage")
    @PreAuthorize("hasAnyRole('CLIENT')")
    public ResponseEntity<Void> updateMypage(
            @AuthenticationPrincipal UserPrincipal user,
            MypageUpdateRequestDto dto
    ) {
        clientService.updateMypage(Long.parseLong(user.getName()), dto);
        return ResponseEntity.noContent().build();
    }


    @PatchMapping("/password")
    @PreAuthorize("hasAnyRole('CLIENT')")
    public ResponseEntity<Void> changePassword(
            @AuthenticationPrincipal UserPrincipal user, ChangePasswordRequestDto dto
    ) {
        clientService.changePassword(Long.parseLong(user.getName()), dto);
        return ResponseEntity.noContent().build();
    }
}
