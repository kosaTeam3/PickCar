package com.erp.domain.client.service;

import com.erp.domain.client.dto.request.RegisterClientRequestDto;
import com.erp.domain.client.entity.Client;
import com.erp.domain.client.repository.ClientRepository;
import com.erp.global.jwt.JwtTokenProvider;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class ClientService {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;

    private final ClientRepository clientRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    // email 중복조회
    public boolean checkEmailDuplicate(String email){
        return clientRepository.existsByEmail(email);
    }

    // 회원가입
    @Transactional
    public void registerClient(RegisterClientRequestDto dto) {

        if(clientRepository.existsByEmail(dto.email())){
            throw new RuntimeException("이미 존재하는 이메일입니다.");
        }
        String encodedPassword = passwordEncoder.encode(dto.password());


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
