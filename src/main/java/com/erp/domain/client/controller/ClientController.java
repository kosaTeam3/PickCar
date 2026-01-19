package com.erp.domain.client.controller;

import com.erp.domain.client.dto.request.EmailCheckRequestDto;
import com.erp.domain.client.dto.request.LoginRequestDto;
import com.erp.domain.client.dto.request.RegisterClientRequestDto;
import com.erp.domain.client.service.ClientService;
import com.erp.global.jwt.JwtTokenProvider;
import com.erp.global.jwt.TokenInfo;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/client")
@RequiredArgsConstructor
public class ClientController {

    private final ClientService clientService;
    private final JwtTokenProvider jwtTokenProvider;

    // 로그아웃
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@RequestHeader("Authorization") String accessToken) {

        // "Bearer " 문자열 떼기 (공백까지 7)
        String token = accessToken.substring(7);

        //  토큰에서 사용자 이메일 추출 (누구껀지 알아야 리프레시 토큰을 지움)
        Authentication auth = jwtTokenProvider.getAuthentication(token);

        //  토큰과 이메일을 서비스로 넘겨서 처리
        clientService.logout(token, auth.getName());

        return ResponseEntity.ok().build();
    }

//    // 토큰 재발급 - refreshToken
//    @PostMapping("/reissue")
//    public ResponseEntity<TokenInfo> reissue(@RequestBody ReissueRequestDto requestDto) {
//        return ResponseEntity.ok(clientService.reissue(requestDto));
//    }

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
