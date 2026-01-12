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
public class CustomUserDetailsService implements UserDetailsService {
    /*
     * UserDetailsService
     * Repository를 이용해 유저를 찾고 만들기
     */

    private final ClientRepository clientRepository;


    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // 1. ClientRepository를 이용해  DB에서 유저 찾기
        return clientRepository.findByEmail(email)
                .map(this::createUserDetails)
                .orElseThrow(() -> new UsernameNotFoundException("해당하는 유저를 찾을 수 없습니다."));
    }

    // 2. Client Entity -> UserDetails 변환 메서드
    private UserDetails createUserDetails(Client client) {
        return User.builder()
                .username(client.getEmail())
                .password(client.getPassword())  // db에 있는 암호화된 비번이어야 함
                .roles("employee")  // Security Config와 맞춰야 함
                .build();
    }
}
