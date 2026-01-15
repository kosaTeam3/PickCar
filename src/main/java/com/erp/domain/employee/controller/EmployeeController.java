package com.erp.domain.employee.controller;


import com.erp.domain.employee.dto.RegisterEmployeeRequestDto;
import com.erp.domain.employee.dto.UpdateEmployeeRequestDto;
import com.erp.domain.employee.service.EmployeeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/manager")
public class EmployeeController {


    private final EmployeeService employeeService;


    // 직원 삭제(퇴사) - Soft Delete
    @DeleteMapping("/employees/{employeeId}")
    public ResponseEntity<Void> deleteEmployee(
            @PathVariable Long employeeId) {

        employeeService.deleteEmployee(employeeId);

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
