package com.erp.global.auth;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "refresh_token")
public class RefreshToken {

    @Id
    @Column(name = "rt_key")
    private String key;  // 사용자의 이메일 (PK)

    @Column(name = "rt_value", length = 512)
    private String value;  // 실제 Refresh Token 값

    // 토큰 갱신
    public RefreshToken updateValue(String token) {
        this.value = token;
        return this;
    }

}
