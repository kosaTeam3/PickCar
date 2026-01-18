package com.erp.domain.accident.controller;

import com.erp.domain.accident.dto.request.AccidentRequest;
import com.erp.domain.accident.dto.response.AccidentDetailResponse;
import com.erp.domain.accident.service.AccidentService;
import lombok.RequiredArgsConstructor;
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

    @GetMapping("/{accidentId}")
    public ResponseEntity<AccidentDetailResponse> getAccidentDetail(@PathVariable Long accidentId) {
        AccidentDetailResponse response = accidentService.getAccidentDetail(accidentId);
        return ResponseEntity.ok(response);
    }
}
