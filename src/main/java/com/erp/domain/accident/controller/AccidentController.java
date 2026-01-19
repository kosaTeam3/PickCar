package com.erp.domain.accident.controller;

import com.erp.domain.accident.dto.request.AccidentRequest;
import com.erp.domain.accident.dto.request.AccidentSearchRequest;
import com.erp.domain.accident.dto.response.AccidentResponse;
import com.erp.domain.accident.service.AccidentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/manager/accident")
@RequiredArgsConstructor
public class AccidentController {
    private final AccidentService accidentService;

    @PostMapping("/{carId}")
    public ResponseEntity<Void> createAccident(
            @PathVariable Long carId,
            @RequestBody AccidentRequest request
            ) {
        Long id = accidentService.createAccident(carId, request);

        return ResponseEntity.created(URI.create("/api/manager/accident/" + id)).build();
    }

    @GetMapping
    public ResponseEntity<Page<AccidentResponse>> getAccidents(
            @ModelAttribute AccidentSearchRequest searchRequest,
            @PageableDefault(sort = "time", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        Page<AccidentResponse> response = accidentService.getAccidents(searchRequest, pageable);

        return ResponseEntity.ok(response);
    }
}
