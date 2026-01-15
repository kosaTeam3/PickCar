package com.erp.domain.employee.service;


import com.erp.domain.branch.entity.Branch;
import com.erp.domain.branch.repository.BranchRepository;
import com.erp.domain.employee.dto.RegisterEmployeeRequestDto;
import com.erp.domain.employee.dto.UpdateEmployeeRequestDto;
import com.erp.domain.employee.entity.Employee;
import com.erp.domain.employee.repository.EmployeeRepository;
import com.erp.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final BranchRepository branchRepository;
    private final PasswordEncoder passwordEncoder;

    private static final String COMPANY_PREFIX = "PC";
    private static final int PHONE_LAST_DIGIT_LENGTH = 4;
    private static final String SEQUENCE_FORMAT = "%04d";

    // 직원 정보 수정
    @Transactional
    public void updateEmployee (Long employeeId, UpdateEmployeeRequestDto request){

        // 직원 조회 (없으면 404)
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new CustomException(404, "해당 직원이 없습니다."));

        // 2. 지점변경이 있을 수 있으니 지점 조회
        Branch branch = null;
        if (request.branchId() != null) {
            // 지점 ID가 들어온다는 건 지점을 옮기겠다는 것 -> 그 때는 DB 조회
            branch = branchRepository.findById(request.branchId())
                    .orElseThrow(()-> new CustomException(404, "해당 지점이 없습니다."));
        }

        // 3. 정보 변경 (Entity의 메서드 호출)
        // JPA가 변경사항을 감지하는 Dirty Checking
        // 변경사항에 Null이 포함되더라도 알아서 거름
        employee.update(
                branch,
                request.name(),
                request.phoneNumber(),
                request.email(),
                request.grade(),
                request.authority(),
                request.quitDate()
        );
        // 트랜잭션이 알아서 Update 쿼리를 날림 (save 필요없음)

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

        //
        Optional<String> lastEmployee =
                employeeRepository.findLastLoginId(prefix);

        int nextSeq = 1;
//        nextSeq = 1;  // 첫 직원이면 1번으로


        if (lastEmployee.isPresent()) {

            String lastLoginId = lastEmployee.get();
            // PC20260010005 에서 뒤에 4자리 (0005)만 잘라 +1 -> 6
            String lastSeqStr = lastLoginId.substring(lastLoginId.length() - PHONE_LAST_DIGIT_LENGTH);
            nextSeq = Integer.parseInt(lastSeqStr) + 1;
        }


        // %04d : 빈 자리를 0으로 채우는 숫자 포맷
        return prefix + String.format(SEQUENCE_FORMAT, nextSeq);
    }
}