package com.erp.global.config;

import com.erp.global.jwt.JwtAuthenticationFilter;
import com.erp.global.jwt.JwtTokenProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    public SecurityConfig(AuthenticationConfiguration authenticationConfiguration) {

    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, JwtTokenProvider jwtTokenProvider) throws Exception {
        return http
                .formLogin(form -> form.disable())  // Spring 로그인 화면 미사용
                .httpBasic(basic -> basic.disable())  // HTTP basic 인증 비활성
                .csrf(csrf -> csrf.disable())  // JWT 사용시 CSRF 보호 비활성

                // 엔드포인트 접근 권한 설정
                .authorizeHttpRequests(
                        auth -> auth
                                .requestMatchers("/api/client/login", "/api/client/register").permitAll()
                                .requestMatchers("/employee").hasRole("employee")  //employee 경로는 직원만
                                .anyRequest().authenticated() // 그 외 모든 요청은 인증 필요
                )
                // JWT 필터 추가( 기존 UsernamePasswordAuthenticationFilter 이전에 실행)
                .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider),
                        UsernamePasswordAuthenticationFilter.class)
                .sessionManagement(
                        session
                                -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .build();
    }

    /**
     * Spring Security의 AuthenticationManager
     * -> Spring이 알아서 UserDetailsService와 PasswordEncoder를 가져다 씀
     * 로그인 시 사용자의 인증(Authentication) 담당
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration)
            throws Exception {
        return configuration.getAuthenticationManager();
    }

    // 비밀번호 암호화
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
