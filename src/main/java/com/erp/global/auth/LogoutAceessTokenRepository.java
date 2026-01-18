package com.erp.global.auth;

import org.springframework.data.jpa.repository.JpaRepository;

public interface LogoutAceessTokenRepository extends JpaRepository<LogoutAccessToken, String> {
    // 토큰이 블랙리스트에 있는지 확인 (있으면 true, 없으면 false)
    boolean existsById(String token);
}
