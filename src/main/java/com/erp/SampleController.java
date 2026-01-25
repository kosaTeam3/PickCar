package com.erp;

import com.erp.domain.employee.dto.request.RegisterEmployeeRequestDto;
import com.erp.domain.employee.entity.EmployeeAuthority;
import com.erp.domain.employee.service.EmployeeService;
import com.erp.global.dto.ErrorResponse;
import com.sun.security.auth.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <H1>응답 견본입니다</H1>
 * 개발끝나고 지워주세오
 */
@RestController("/")
@RequiredArgsConstructor
public class SampleController {

    private final EmployeeService employeeService;

    @GetMapping("/testemp")
    public void get() {
        employeeService.createEmployee(RegisterEmployeeRequestDto.builder()
                .branchId(1L)
                .name("홍길동")
                .phoneNumber("010-1234-5678")
                .email("hong@test.com")
                .grade("사원")
                .authority(EmployeeAuthority.ADMIN)
                .build());
    }

    // 특정 권한을 가진 사람만 허용합니다.
//    @PreAuthorize("hasAnyRole('ADMIN','MANAGER','STAFF')") // 권한이 없는 경우 404 예외를 발생시킵니다
    @GetMapping("/getRequestUserId")
    // 요청한 사용자 정보를 가져올때 견본입니다.
    public String test(@AuthenticationPrincipal UserPrincipal user) {
        return user.getName();
    }

    //데이터가 존재할경우 견본입니다
    @GetMapping("/data")
    public List<String> withData() {
        return List.of("gd");
    }

    //데이터가 없을때 견본입니다.
    @GetMapping("/nodata")
    public ResponseEntity<Void> nodata() {
        return ResponseEntity.noContent().build();
    }

    //예외를 확인하기 위한 코드입니다
    @GetMapping("/e")
    public ResponseEntity<ErrorResponse> isError() {
        throw new RuntimeException("펑펑");
    }

}
