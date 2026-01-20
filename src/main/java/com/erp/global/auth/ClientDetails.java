package com.erp.global.auth;

import com.erp.domain.client.entity.Client;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

@RequiredArgsConstructor
public class ClientDetails implements UserDetails {

    private final Client client;

    // Controller에서 '@AuthenticationPrincipal'로 꺼낼 때 필요
    public Client getClient() {

        return client;
    }

    // 권한
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // 고객은 Client 권한
        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_CLIENT"));
    }

    // Spring security가 비밀번호 대조할 때 필요
    @Override
    public String getPassword() {
        return client.getPassword();
    }

    // Security는 ID를 username이라고 셋팅되어있을 뿐 우린 email을 씀
    @Override
    public String getUsername() {
        return client.getEmail();
    }


    // 일단 권한 다 통과로 설정 후 개발
    @Override
    public boolean isAccountNonExpired() {
//        return UserDetails.super.isAccountNonExpired();
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
//        return UserDetails.super.isAccountNonLocked();
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
//        return UserDetails.super.isCredentialsNonExpired();
        return true;
    }

    @Override
    public boolean isEnabled() {
//        return UserDetails.super.isEnabled();
        return true;
    }
}
