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
    /**
     * UserDetailsService
     * Repository를 이용해 유저를 찾고 만들기
     */

    private final ClientRepository clientRepository;


    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        return null;
    }


    private UserDetails createUserDetails(Client client){
        return User.builder()
                .username(client.getEmail())
                .password(client.getPassword())
                .roles("employee")
                .build();
    }
}
