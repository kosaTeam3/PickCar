package com.erp.domain.employee.service;

import com.erp.domain.branch.entity.Branch;
import com.erp.domain.branch.repository.BranchRepository;
import com.erp.domain.employee.dto.RegisterEmployeeRequestDto;
import com.erp.domain.employee.entity.BranchCode;
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


    // 직원 생성
    @Transactional
    public Long createEmployee(RegisterEmployeeRequestDto request){

        // 이메일 중복 검사
        // 존재하면 CustomException
        if (employeeRepository.existsByEmail(request.email())){
            throw new CustomException(409, "이미 존재하는 이메일입니다.");
        }

        // 지점 조회
        Branch branch = branchRepository.findById(request.branchId())
                .orElseThrow(() -> new CustomException(401, "지점이 없습니다."));

        // 사번 생성
        String loginId = generateLoginId(branch.getName(), request.entryDate());

        String phone = request.phoneNumber().replace("-", "");

        // 초기 비밀번호 생성
        String tempPw = loginId + phone.substring(phone.length() - 4) + "!";

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
                .joinChangePassword(true)
                .build();
        // 저장하고 ID 반환
        return employeeRepository.save(employee).getId();
    }

    // 사원 번호 생성
    private String generateLoginId(String branchName, LocalDate entryDate){

        // 사번에 들어갈 연도
        int year = entryDate.getYear();

        // 사번에 들어갈 지점코드
        String branchCode = BranchCode.getCodeByName(branchName);

        // 사번 prefix
        String prefix = "PC" + year + branchCode;

        //
        Optional<Employee> lastEmployee =
                employeeRepository.findTopByLoginIdStartingWithOrderByLoginIdDesc(prefix);

        int nextSeq;

        if (lastEmployee.isEmpty()){
            nextSeq = 1;  // 첫 직원이면 1번으로
        } else {
            // 마지막 사번 가져오기 ex) PC20260010005
            String lastLoginId = lastEmployee.get().getLoginId();

            //  lastLoginId.length() -4 = 끝에서 4번째 자리부터 시작하란 것
            String lastSeqStr = lastLoginId.substring(lastLoginId.length() - 4);

            // 숫자로 파싱해서 + 1  ex) 0005 -> 5 -> 6
            nextSeq = Integer.parseInt(lastSeqStr) + 1;
        }

        //  숫자를 다시 4자리 문자열로 바꾸기 (6 -> "0006")
        // %04d : 빈 자리를 0으로 채우는 숫자 포맷
        return prefix + String.format("%04d", nextSeq);
    }
}
