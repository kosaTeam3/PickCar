package com.erp.domain.client.controller;

import com.erp.domain.client.dto.request.EmailCheckRequestDto;
import com.erp.domain.client.dto.request.LoginRequestDto;
import com.erp.domain.client.dto.request.RegisterClientRequestDto;
import com.erp.domain.client.service.ClientService;
import com.erp.global.jwt.TokenInfo;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/client")
@RequiredArgsConstructor
public class ClientController {

    private final ClientService clientService;

    // 이메일 중복 확인
    @PostMapping("/validation")
    public ResponseEntity<String> checkEmail(@RequestBody EmailCheckRequestDto requestDto) {

        boolean isDuplicate = clientService.checkEmailDuplicate(requestDto.email());

        if (isDuplicate) {
            return ResponseEntity.status(409).body("이미 존재하는 이메일입니다.");
        }
        return ResponseEntity.ok("사용 가능한 이메일입니다.");
    }

    // 로그인 페이지
    @PostMapping("/login")
    public TokenInfo login(@RequestBody LoginRequestDto loginRequestDto) {
        return clientService.login(loginRequestDto);
    }

    // 회원가입
    @PostMapping("/register")
    public ResponseEntity<String> registerClient(@Valid @RequestBody RegisterClientRequestDto requestDto) {

        clientService.registerClient(requestDto);
        return ResponseEntity.noContent().build();
    }
}
