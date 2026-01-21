package com.erp.global.config;

import com.erp.global.auth.ClientUserDetailsService;
import com.erp.global.jwt.JwtAuthenticationFilter;
import com.erp.global.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final ClientUserDetailsService clientUserDetailsService;
    // todo  employee 추가 후 필요
    // private final EmployeeUserDetailsService employeeService;


    // 비밀번호 암호화 빈 등록
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Spring Security의 AuthenticationManager
    // -> Spring이 알아서 UserDetailsService와 PasswordEncoder를 가져다 씀 ->AuthenticationConfiguration
    // -> Spring에게 명시적으로 검증방법이 2개 (client, employee) 있다고 알려주기 -> HttpSecurity
    // 로그인 시 사용자의 인증(Authentication) 담당
    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity httpSecurity)
            throws Exception {
        AuthenticationManagerBuilder builder =   // getSharedObject: 스프링 시큐리티가 공유해서 쓰는 객체 보관
                httpSecurity.getSharedObject(AuthenticationManagerBuilder.class);
        //  client
        builder.userDetailsService(clientUserDetailsService)
                .passwordEncoder(bCryptPasswordEncoder());

        // employee
        // builder.userDetailsService(employeeUserDetailsService)
        // .passwordEncoder(bCryptPasswordEncoder());
        return builder.build();
    }


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, JwtTokenProvider jwtTokenProvider) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)  // JWT 사용시 CSRF 보호 필요없음
                .formLogin(AbstractHttpConfigurer::disable)  // Spring 로그인 화면 미사용(Rest API니까)
                .httpBasic(AbstractHttpConfigurer::disable)  // HTTP basic 인증 비활성

                .sessionManagement( // session 사용 안 함 (Stateless 설정)
                        session
                                -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                // URL 접근 권한 설정
                .authorizeHttpRequests(
                        auth -> auth
                                .requestMatchers( // 로그인, 회원가입, 재발급, 이메일 체크는 누구나 가능
                                        // Client
                                        "/api/client/login",      // 로그인
                                        "/api/client/register",   // 회원가입
                                        "/api/client/reissue",    // 토큰 재발급
                                        "/api/client/validation",  // 이메일 중복 체크
                                        "/payment-test.html" // 결제 테스트
                                ).permitAll()
                                // 관리자 페이지는 나중에 권한 처리 (일단 인증된 사람들만)
//                                .requestMatchers("/api/manager/**").hasRole("employee")
                                .requestMatchers("/api/manager/**").permitAll()
//                                .anyRequest().authenticated() // 그 외 모든 요청은 인증 필요
                                .anyRequest().permitAll()
                )
//                .oauth2Login() // 추후 Oauth

                // JWT 필터 추가( 기존 UsernamePasswordAuthenticationFilter 이전에 실행)
                // ID/PW 검사 전 토큰부터 검사
                .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider),
                        UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}


