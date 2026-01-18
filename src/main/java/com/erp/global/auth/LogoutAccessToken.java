package com.erp.global.auth;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "logout_access_token")
public class LogoutAccessToken {

    @Id
    private String id; // AccessToken 값
    private String email; // 누가 로그아웃 했는지
    private Long expiration; // 언제 삭제될지 (남은 시간)
}

