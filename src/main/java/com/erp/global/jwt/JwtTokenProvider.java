package com.erp.global.jwt;

import com.erp.domain.client.entity.Client;
import com.erp.domain.employee.entity.Employee;
import com.sun.security.auth.UserPrincipal;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

@Component
@Slf4j
public class JwtTokenProvider {

    private final Key key;

    // 토큰 만료시간
    @Value("${jwt.access_expiration}")
    private long accessExpirationTime;

    @Value("${jwt.refresh_expiration}")
    private long refreshExpirationTime;

    // 1. 암호화 키 셋팅 : : application.yml`에서 가져온 비밀키 사용
    public JwtTokenProvider(@Value("${jwt.secret_key}") String secretKey) {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    // Client Login
    public TokenInfo generateClientToken(Client client) {
        String accessToken = Jwts.builder()
                .claim("id", client.getId())
                .claim("authority", "ROLE_CLIENT")
                .setExpiration(new Date(System.currentTimeMillis() + accessExpirationTime))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
        return TokenInfo.builder()
                .grantType("Bearer")
                .accessToken(accessToken)
                .build();
    }

    //  Employee Login
    public TokenInfo generateToken(Employee employee) {
        String accessToken = Jwts.builder()
                .claim("id", employee.getId())
                .claim("isFirstLogin", employee.getPasswordChangeRequired())
                .claim("authority", "ROLE_" + employee.getAuthority())
                .setExpiration(new Date(System.currentTimeMillis() + accessExpirationTime))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        return TokenInfo.builder()
                .grantType("Bearer")
                .accessToken(accessToken)
                .build();
    }

    // Token Info 추출 : 토큰 복호화(암호 역으로 풀기) - 누구의 것인지 알아내기
    public Authentication getAuthentication(String accessToken) {
        Claims claims = parseClaim(accessToken);

        if (claims.get("authority") == null) {
            throw new RuntimeException("권한 정보가 없는 토큰입니다.");
        }

        // 권한 정보 획득
        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(claims.get("authority").toString().split(","))
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());

        // UserDetails 객체 만들어서 Authenticaiton 리턴
        // UserDetails : interface, User : UserDetails를 구현한 class
        UserPrincipal principal = new UserPrincipal(claims.get("id").toString());
        return new UsernamePasswordAuthenticationToken(principal, "", authorities);
    }

    // 토큰 검증
    public boolean validateToken(String token) {

        try {
            // 파싱시도해서 에러가 안 나면 유효토큰
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (io.jsonwebtoken.security.SignatureException | MalformedJwtException e) {
            log.info("잘못된 JWT 서명입니다."); // 해킹시도 or 변조된 토큰
        } catch (ExpiredJwtException e) {
            log.info("만료된 JWT 토큰입니다.");
        } catch (UnsupportedJwtException e) {
            log.info("JWT 토큰이 잘못되었습니다. "); // 토큰이 비어있거나 형식이 이상함
        }
        return false;
    }

    // 토큰 파싱
    private Claims parseClaim(String accessToken) {
        try {
            return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(accessToken).getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims(); // 만료후에도 꺼내기
        }
    }
}
