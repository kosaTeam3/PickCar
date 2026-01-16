package com.erp.domain.employee.controller;


import com.erp.domain.employee.dto.request.RegisterEmployeeRequestDto;
import com.erp.domain.employee.dto.request.UpdateEmployeeRequestDto;
import com.erp.domain.employee.dto.response.EmployeeListResponse;
import com.erp.domain.employee.service.EmployeeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/manager")
public class EmployeeController {


    private final EmployeeService employeeService;

    // 직원 전체 조회(퇴사자 미포함)
    @GetMapping("/employees")
    public ResponseEntity<Page<EmployeeListResponse>> getEmployees(
            @PageableDefault(sort = "id", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        return ResponseEntity.ok(employeeService.getEmployeeList(pageable));
    }

    // 직원 삭제(퇴사) - Soft Delete
    @DeleteMapping("/employees/{employeeId}")
    public ResponseEntity<Void> deleteEmployee(
            @PathVariable Long employeeId,
            @RequestParam LocalDate date
    ) {
        employeeService.deleteEmployee(employeeId, date);

        return ResponseEntity.noContent().build();  // 204
    }

    // 직원 수정
    @PatchMapping("/employees/{employeeId}")
    public ResponseEntity<Void> updateEmployee(
            @PathVariable Long employeeId,
            @Valid @RequestBody UpdateEmployeeRequestDto requestDto) {

        employeeService.updateEmployee(employeeId, requestDto);

        return ResponseEntity.ok().build();
    }

    // 직원 생성
    @PostMapping("/employees")
    public ResponseEntity<Long> createEmployee(
            @Valid @RequestBody RegisterEmployeeRequestDto requestDto) {

        long newEmployeeId = employeeService.createEmployee(requestDto);
        return ResponseEntity.ok(newEmployeeId);
    }
}
