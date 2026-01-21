package com.erp.domain.client.service;

import com.erp.domain.client.dto.request.LoginRequestDto;
import com.erp.domain.client.dto.request.RegisterClientRequestDto;
import com.erp.domain.client.entity.Client;
import com.erp.domain.client.entity.Gender;
import com.erp.domain.client.repository.ClientRepository;
import com.erp.global.exception.CustomException;
import com.erp.global.jwt.JwtTokenProvider;
import com.erp.global.jwt.TokenInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class ClientService {

    private final JwtTokenProvider jwtTokenProvider;
    private final ClientRepository clientRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    // 로그인
    @Transactional
    public TokenInfo login(LoginRequestDto dto) {
        Client client =
                clientRepository.findByEmail(dto.email()).orElseThrow(
                        () -> new CustomException(401, "사용자를 찾을 수 없습니다.")
                );
        if (passwordEncoder.matches(dto.password(), client.getPassword())) {
            throw new CustomException(401, "사용자를 찾을 수 없습니다.");
        }
        return jwtTokenProvider.generateClientToken(client);
    }

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
