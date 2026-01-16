package com.erp.domain.accident.controller;

import com.erp.domain.accident.dto.request.AccidentRequest;
import com.erp.domain.accident.dto.response.AccidentResponse;
import com.erp.domain.accident.service.AccidentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

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
    public ResponseEntity<List<AccidentResponse>> getAccidents() {
        List<AccidentResponse> response = accidentService.getAccidents();

        return ResponseEntity.ok(response);
    }
}
