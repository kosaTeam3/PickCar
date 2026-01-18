package com.erp.global.auth;


import com.erp.domain.client.entity.Client;
import com.erp.domain.client.repository.ClientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ClientUserDetailsService implements UserDetailsService {
    /*
     * UserDetailsService
     * Repository를 이용해 유저를 찾고 만들기
     */

    private final ClientRepository clientRepository;


    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // 1. ClientRepository를 이용해  DB에서 유저 찾기
        Client client = clientRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("해당 이메일을 가진 고객이 없습니다." + email));

        // 2. ClientDetails에 담아서 반환
        return new ClientDetails(client);
    }

    // 2. Client Entity -> UserDetails 변환 메서드
    // DB 조회 (DB비번 vs 상용자 입력비번 대조 검증)
    // 인증 완료되면 객체 반환
    // 한계 : 이 방식은 단일 테이블에 적합, 우리는 Client와 Employee 를 조회하기 때문에
    // 이거말고 따로 필요 (현재 계층 인증 모호함)
    private UserDetails createUserDetails(Client client) {
        return User.builder()
                .username(client.getEmail())
                .password(client.getPassword())  // db에 있는 암호화된 비번이어야 함
                .roles("employee")  // Security Config와 맞춰야 함
                .build();
    }
}
