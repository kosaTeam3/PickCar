package com.erp.domain.client.service;

import com.erp.domain.client.dto.request.LoginRequestDto;
import com.erp.domain.client.dto.request.RegisterClientRequestDto;
import com.erp.domain.client.entity.Client;
import com.erp.domain.client.repository.ClientRepository;
import com.erp.global.jwt.JwtTokenProvider;
import com.erp.global.jwt.TokenInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class ClientService {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;

    private final ClientRepository clientRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    // 로그인
    @Transactional(readOnly = true)
    public TokenInfo login(LoginRequestDto loginRequestDto) {
        //1. 인증되지 않은 ID/PW 객체 생성
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(
                        loginRequestDto.email(),
                        loginRequestDto.password());

        // 2. 실제 검증
        // 여기서 Securityconfig의 Password Encoder 사용
        Authentication authentication = authenticationManager.authenticate(authenticationToken);

        // 3. 인증 정보를 가지고 JWT 토큰 생성
        TokenInfo tokenInfo = jwtTokenProvider.generateToken(authentication);
        return tokenInfo;

    }

    // email 중복조회
    public boolean checkEmailDuplicate(String email) {
        return clientRepository.existsByEmail(email);
    }

    // 회원가입
    @Transactional
    public void registerClient(RegisterClientRequestDto dto) {
        // 1. 중복 검사
        if (clientRepository.existsByEmail(dto.email())) {
            throw new RuntimeException("이미 존재하는 이메일입니다.");
        }
        // 2. 비밀번호 BCrypt 암호화
        String encodedPassword = passwordEncoder.encode(dto.password());

        // 3. Entity 변환 및 저장
        Client client = Client.builder()
                .email(dto.email())
                .password(encodedPassword)
                .phoneNumber(dto.phone_number())
                .name(dto.name())
                .gender(dto.gender())
                .residentNumber(dto.resident_number())
                .birthday(LocalDate.parse(dto.birthday()))
                .blacklisted(false)
                .licenceArea(dto.licence_area())
                .licenceNumber(dto.licence_number())
                .licenceDay(LocalDate.parse(dto.licence_day()))
                .build();
        clientRepository.save(client);
    }
}
