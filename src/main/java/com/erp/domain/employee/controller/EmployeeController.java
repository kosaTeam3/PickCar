package com.erp.domain.employee.controller;

import com.erp.domain.employee.dto.RegisterEmployeeRequestDto;
import com.erp.domain.employee.service.EmployeeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/manager")
public class EmployeeController {

    private final EmployeeService employeeService;

    @PostMapping("/employees")
    public ResponseEntity<Long> createEmployee(@Valid @RequestBody RegisterEmployeeRequestDto requestDto){

        long newEmployeeId = employeeService.createEmployee(requestDto);

        return ResponseEntity.ok(newEmployeeId);
    }
}
