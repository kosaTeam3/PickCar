package com.erp.global.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
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


    // 1. 암호화 키 셋팅 : : application.yml에서 가져온 비밀키 사용
    public JwtTokenProvider(@Value("${jwt.secret_key}") String secretKey) {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    // 2. Create Token  : 유저 정보 받고  AccessToken, RefreshToken 만들기
    public TokenInfo generateToken(Authentication authentication) {
        // 권한 가져오기
        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        long now = (new Date()).getTime();

        // AccessToken 유효기간
        Date accessTokenExpiration = new Date(now + 1800 * 1000L);

        // Create Access Token
        String accessToken = Jwts.builder()
                .setSubject(authentication.getName())  // Payload에 유저네임(email) 저장
                .claim("auth", authorities)  // Payload claim에 권한 정보 젖아
                .setExpiration(accessTokenExpiration)  //만료시간 설정
                .signWith(key, SignatureAlgorithm.HS256)  // Signature
                .compact();


        // Create Refresh Token
        String refreshToken = Jwts.builder()
                .setExpiration(new Date(now + (3600 * 1000L) * 2))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
        return TokenInfo.builder()
                .grantType("Bearer")
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    // 3. Token Info 추출 : 토큰 복호화(암호 역으로 풀기) - 누구의 것인지 알아내기
    public Authentication getAuthentication(String accessToken) {
        // 토큰 복호화
        Claims claims = parseClaim(accessToken);

        if (claims.get("auth") == null) {
            throw new RuntimeException("권한 정보가 없는 토큰입니다.");
        }

        // 권한 정보 획득
        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(claims.get("auth").toString().split(","))
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());

        // UserDetails 객체 만들어서 Authenticaiton 리턴
        // UserDetails : interface, User : UserDetails를 구현한 class
        UserDetails principal = new User(claims.getSubject(), "", authorities);
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
    private Claims parseClaim(String accessToekn) {
        try {
            return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(accessToekn).getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims(); // 만료후에도 꺼내기
        }
    }
}
