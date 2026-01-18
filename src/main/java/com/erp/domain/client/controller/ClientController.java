package com.erp.domain.client.controller;

import com.erp.domain.client.dto.request.EmailCheckRequestDto;
import com.erp.domain.client.dto.request.LoginRequestDto;
import com.erp.domain.client.dto.request.RegisterClientRequestDto;
import com.erp.domain.client.dto.request.ReissueRequestDto;
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

    // 토큰 재발급
    @PostMapping("/reissue")
    public ResponseEntity<TokenInfo> reissue(@RequestBody ReissueRequestDto requestDto) {
        return ResponseEntity.ok(clientService.reissue(requestDto));
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
}
