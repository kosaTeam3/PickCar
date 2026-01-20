package com.erp.domain.client.service;

import com.erp.domain.client.dto.request.LoginRequestDto;
import com.erp.domain.client.dto.request.RegisterClientRequestDto;
import com.erp.domain.client.entity.Client;
import com.erp.domain.client.entity.Gender;
import com.erp.domain.client.repository.ClientRepository;
import com.erp.global.auth.LogoutAccessToken;
import com.erp.global.auth.LogoutAccessTokenRepository;
import com.erp.global.auth.RefreshTokenRepository;
import com.erp.global.exception.CustomException;
import com.erp.global.jwt.JwtTokenProvider;
import com.erp.global.jwt.TokenInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class ClientService {


    private final LogoutAccessTokenRepository logoutAceessTokenRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final ClientRepository clientRepository;
    private final BCryptPasswordEncoder passwordEncoder;


    // 로그아웃
    @Transactional
    public void logout(String accessToken, String email) {

        // 1. Access Token 남은 시간 계산
        Long expiration = jwtTokenProvider.getExpireTime(accessToken);

        // 2. 이미 만료된 토큰이 아니라면 블랙리스트에 저장
        if (expiration > 0) {
            logoutAceessTokenRepository.save(LogoutAccessToken.builder()
                    .id(accessToken)
                    .email(email)
                    .expiration(expiration)
                    .build());
        }

//        // 3. RefreshToken 삭제 (이제 재발급 안 됨)
//        refreshTokenRepository.findByKey(email)
//                .ifPresent(refreshTokenRepository::delete);
    }

    // 로그인
    @Transactional
    public TokenInfo login(LoginRequestDto loginRequestDto) {
        //1. 인증되지 않은 ID/PW 객체 생성
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(
                        loginRequestDto.email(),
                        loginRequestDto.password());

        // 2. 실제 검증
        // AuthenticationManager가 자동으로 ClientUserDetailsService를 호출하고
        // 비밀번호까지 비교해서 검증된 객체인 Authentication을 줍니다
        // 비밀번호가 틀림녀 여기서 예외(AuthenticationException)가 터지고 메서드가 종료됩니다.
        Authentication authentication = authenticationManager.authenticate(authenticationToken);

        // 3. JWT 토큰 발급 - 여기까지 오면 로그인이 성공하고 인증된 정보로 토큰을 생성합니다.
//        TokenInfo tokenInfo = jwtTokenProvider.generateToken(authentication);
        TokenInfo tokenInfo = jwtTokenProvider.generateToken(authentication);

//        리프레시 토큰 DB 저장 로직 (프론트엔드 도입 후 필요)
//        refreshTokenRepository.save(RefreshToken.builder()
//                .key(authentication.getName())
//                .value(tokenInfo.refreshToken())
//                .build());
        return tokenInfo;
    }

//    // 토큰 재발급  - refresh Token 비활성
//    @Transactional
//    public TokenInfo reissue(ReissueRequestDto requestDto) {
//
//        // 1. RefreshToken 유효성 검사 (위조여부)
//        if (!jwtTokenProvider.validateToken(requestDto.refreshToken())) {
//            throw new CustomException(401, "유효하지 않은 Refresh Token입니다.");
//        }
//
//        // 2. Access Token에서 UserEmail
//        // 토큰이 만료가 됐더라도 누구인지는 알아야 하기 때문
//        Authentication authentication = jwtTokenProvider.getAuthentication(requestDto.accessToken());
//
//        // 3. Db에서 그 사람의 RefreshToken 꺼내오기
//        RefreshToken refreshToken = refreshTokenRepository.findByKey(authentication.getName())
//                .orElseThrow(() -> new CustomException(400, "로그아웃 된 사용자 입니다.")); // DB에 없으면 로그아웃 된 것
//        // 4. Refresh Token 일지하는 검사
//        if (!refreshToken.getValue().equals(requestDto.refreshToken())) {
//            throw new CustomException(400, "토큰 유저의 정보가 일치하지 않습니다.");
//        }
//
//        // 5. 새로운 토큰 생성
//        TokenInfo tokenInfo = jwtTokenProvider.generateToken(authentication);
//
//        // 6. DB 정보 업데이트 (새로운 Refresh Token으로 교체)
//        RefreshToken newRefreshToken = refreshToken.updateValue(tokenInfo.refreshToken());
//        refreshTokenRepository.save(newRefreshToken);
//
//        return tokenInfo;
//    }

    // email 중복조회
    @Transactional
    public void checkEmailDuplicate(String email) {
        if (clientRepository.existsByEmail(email)) {
            throw new CustomException(409, "이미 존재하는 이메일입니다.");
        }
    }

    // 회원가입
    @Transactional
    public void registerClient(RegisterClientRequestDto dto) {
        // 1. 중복 검사
        if (clientRepository.existsByEmail(dto.email())) {
            throw new CustomException(404, "이미 존재하는 이메일입니다.");
        }
        // 2. 비밀번호 BCrypt 암호화
        String encodedPassword = passwordEncoder.encode(dto.password());

        // 3. 주민으로 생년월일, 성별 변환

        String residentNumber = dto.residentNumber();  // 주민번호 가져오기
        LocalDate birthDate = getBirthDateFromRegiNum(residentNumber);
        Gender gender = getGenderFromResiNum(dto.residentNumber());


        // 4. Entity 변환 및 저장
        Client client = Client.builder()
                .email(dto.email())
                .password(encodedPassword)
                .phoneNumber(dto.phoneNumber())
                .name(dto.name())
                .gender(gender)  // 주민에서 추출된 성별
                .residentNumber(dto.residentNumber())
                .birthday(birthDate)  // 주민에서 추출된 생년월일
                .licenceArea(dto.licenceArea())
                .licenceNumber(dto.licenceNumber())
                .licenceDay(LocalDate.parse(dto.licenceDay()))
                .build();
        clientRepository.save(client);
    }

    // 성별 추출
    private Gender getGenderFromResiNum(String residentNumber) {

        char genderCode = residentNumber.charAt(7);
        // 남자는 홀수 , 여자는 짝수
        if (genderCode == '1' ||
                genderCode == '3' ||
                genderCode == '5' ||
                genderCode == '7') {
            return Gender.MALE;
        } else if (genderCode == '2' ||
                genderCode == '4' ||
                genderCode == '6' ||
                genderCode == '8') {
            return Gender.FEMALE;
        } else {
            throw new CustomException(404, "성별번호가 잘못되었습니다.");
        }
    }

    // 생년월일 추출
    private LocalDate getBirthDateFromRegiNum(String residentNumber) {
        // YYMMDD
        String brnum = residentNumber.substring(0, 6);
        // 뒷자리 첫 번째 숫자
        char genderCode = residentNumber.charAt(7);

        int year = Integer.parseInt(brnum.substring(0, 2));
        int month = Integer.parseInt(brnum.substring(2, 4));
        int day = Integer.parseInt(brnum.substring(4, 6));

        // 1,2,5,6은 1900년대   /  3,4,7,8은 2000년대
        if (genderCode == '1' ||
                genderCode == '2' ||
                genderCode == '5' ||
                genderCode == '6') {
            year += 1900;
        } else if (genderCode == '3' ||
                genderCode == '4' ||
                genderCode == '7' ||
                genderCode == '8') {
            year += 2000;
        }
        return LocalDate.of(year, month, day);
    }

}
