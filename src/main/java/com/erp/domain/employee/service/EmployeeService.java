package com.erp.domain.employee.service;


import com.erp.domain.branch.entity.Branch;
import com.erp.domain.branch.repository.BranchRepository;
import com.erp.domain.employee.dto.request.*;
import com.erp.domain.employee.dto.response.EmployeeListResponse;
import com.erp.domain.employee.entity.Employee;
import com.erp.domain.employee.repository.EmployeeRepository;
import com.erp.global.exception.CustomException;
import com.erp.global.jwt.JwtTokenProvider;
import com.erp.global.jwt.TokenInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@Transactional
@RequiredArgsConstructor
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final BranchRepository branchRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;

    private static final String COMPANY_PREFIX = "PC";
    private static final int PHONE_LAST_DIGIT_LENGTH = 4;
    private static final String SEQUENCE_FORMAT = "%04d";


    // 직원 전체조회 + 페이징 + Search
    // 비밀번호 변경 ( 첫 로그인시 강제 변경 포함)
    public void changePassword(Long employeeId, PasswordChangeRequestDto requestDto) {

        // 1. 직원 조회
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new CustomException(404, "해당 직원이 없습니다."));

//        // 2. 새 비번 , 확인 비번 일치여부 검증 - 프론트 엔드 역할
//        if (!requestDto.newPassword().equals(requestDto.checkPassword())){
//            throw new CustomException(400, "비밀번호가 일치하지 않습니다.");
//        }

        // 3. 기존 비밀번호와 동일한지 검증
        if (passwordEncoder.matches(requestDto.newPassword(), employee.getPassword())) {
            throw new CustomException(400, "기존 비밀번호와 동일하게 변경할 수 없습니다.");
        }

        // 4. 비밀번호 암호화 및 변경
        String encodedPw = passwordEncoder.encode(requestDto.newPassword());
        employee.setPassword(encodedPw);

        // 5. DB의 join_change_password 컬럼을 0(false)로 변경
        // 이로서 초기 비밀번호 변경 완료
        employee.setPasswordChangeRequired(false);
    }

    // 직원 전체조회 + 페이징
    @Transactional(readOnly = true)  // 조회 전용
    public Page<EmployeeListResponse> getEmployeeList(EmployeeSearchRequest request, Pageable pageable) {
        if (request.quit() == null || !request.quit()) {

            return employeeRepository.findAllByQuitDateIsNull(
                            request.name(),
                            request.email(),
                            request.call(),
                            request.grade(),
                            request.entryDate(),
                            pageable)
                    .map(this::toListDto);
        } else
            return employeeRepository.findAllNotQuit(
                            request.name(),
                            request.email(),
                            request.call(),
                            request.grade(),
                            request.entryDate(),
                            pageable)
                    .map(this::toListDto);
    }

    // 직원 퇴사 (삭제)
    public void deleteEmployee(Long employeeId, LocalDate date) {

        // 조회 (검증)
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new CustomException(404, "해당 직원이 없습니다."));

        // 퇴사 처리 (DB 상태변경)
        employee.resign(date);
        //  JPA의 Dirty Checking에 의해 트랜잭션 종료 시 Update 쿼리 실행
    }

    // 직원 정보 수정
    public void updateEmployee(Long employeeId, UpdateEmployeeRequestDto request) {

        // 직원 조회 (없으면 404)
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new CustomException(404, "해당 직원이 없습니다."));

        // 2. 지점변경이 있을 수 있으니 지점 조회
        if (request.branchId() != null) {
            // 지점 ID가 들어온다는 건 지점을 옮기겠다는 것 -> 그 때는 DB 조회
            employee.setBranch(branchRepository.findById(request.branchId())
                    .orElseThrow(() -> new CustomException(404, "해당 지점이 없습니다."))
            );
        }

        // 3. 정보 변경 (Entity의 메서드 호출)
        if (request.name() != null) {
            employee.setName(request.name());
        }

        if (request.phoneNumber() != null) {
            employee.setPhoneNumber(request.phoneNumber());
        }

        if (request.email() != null) {
            employee.setEmail(request.email());
        }

        if (request.grade() != null) {
            employee.setGrade(request.grade());
        }

        if (request.authority() != null) {
            employee.setAuthority(request.authority());
        }

        if (request.quitDate() != null) {
            employee.setQuitDate(request.quitDate());
        }
        // 트랜잭션이 알아서 Update 쿼리를 날림 (save 필요없음) - Dirty Checking
    }

    // 직원 생성
    @Transactional
    public Long createEmployee(RegisterEmployeeRequestDto request) {

        // 이메일 중복 검사
        if (employeeRepository.existsByEmail(request.email())) {
            throw new CustomException(409, "이미 존재하는 이메일입니다.");
        }

        // 지점 조회
        Branch branch = branchRepository.findById(request.branchId())
                .orElseThrow(() -> new CustomException(404, "지점이 없습니다."));

        // 사번 생성
        String loginId = generateLoginId(branch, request.entryDate());

        String phone = request.phoneNumber().replace("-", "");

        // 초기 비밀번호 생성
        String tempPw = loginId + phone.substring(phone.length() - PHONE_LAST_DIGIT_LENGTH) + "!";

        // 비밀번호 암호화

        String encodedPw = passwordEncoder.encode(tempPw);

        //  Entity 생성
        Employee employee = Employee.builder()
                .branch(branch)
                .name(request.name())
                .phoneNumber(request.phoneNumber())
                .email(request.email())
                .grade(request.grade())
                .authority(request.authority())
                .entryDate(request.entryDate())
                .loginId(loginId)
                .password(encodedPw)
                .passwordChangeRequired(true)
                .build();
        // 저장하고 ID 반환
        return employeeRepository.save(employee).getId();
    }

    // 사원 번호 생성
    private String generateLoginId(Branch branch, LocalDate entryDate) {

        // 사번에 들어갈 연도
        int year = entryDate.getYear();

        // 사번에 들어갈 지점코드
        String branchCode = String.format("%03d", branch.getId());

        // 사번 prefix
        String prefix = COMPANY_PREFIX + year + branchCode;

        Long employeeCount = employeeRepository.countByBranch_Id(branch.getId()) + 1;

        // %04d : 빈 자리를 0으로 채우는 숫자 포맷
        return prefix + String.format(SEQUENCE_FORMAT, employeeCount);
    }

    private EmployeeListResponse toListDto(Employee entity) {
        return EmployeeListResponse.builder()
                .employId(entity.getId())
                .employName(entity.getName())
                .employCall(entity.getPhoneNumber())
                .employGrade(entity.getGrade())
                .branchId(entity.getBranch().getId())
                .entryDate(entity.getEntryDate())
                .quitDate(entity.getQuitDate())
                .loginId(entity.getLoginId())
                .build();
    }

    @Transactional(readOnly = true)
    public TokenInfo login(EmployeeLoginRequestDto dto) {
        Employee employee = employeeRepository.findByLoginId(dto.loginId()).orElseThrow(
                () -> new CustomException(401, "사용자를 찾을 수 없습니다")
        );

        if (!passwordEncoder.matches(dto.password(), employee.getPassword())) {
            throw new CustomException(401, "사용자를 찾을 수 없습니다");
        }
        return jwtTokenProvider.generateToken(employee);
    }
}









