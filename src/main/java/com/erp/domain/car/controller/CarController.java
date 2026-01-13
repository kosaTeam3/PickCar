package com.erp.domain.car.controller;

import com.erp.domain.car.dto.request.CarCreateRequest;
import com.erp.domain.car.dto.request.CarUpdateRequest;
import com.erp.domain.car.dto.response.CarDetailResponse;
import com.erp.domain.car.dto.response.CarListResponse;
import com.erp.domain.car.service.CarService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/manager/vehicles")
@RequiredArgsConstructor
public class CarController {

    private final CarService carService;

    /* 차량 등록 */
    @PostMapping
    public ResponseEntity<Long> createCar(@RequestBody CarCreateRequest request) {
        Long carId = carService.createCar(request);
        return ResponseEntity.ok(carId);
    }

    /* 차량 수정 */
    @PutMapping("/{carId}")
    public ResponseEntity<Void> updateCar(@RequestBody CarUpdateRequest request, @PathVariable Long carId) {
        carService.updateCar(carId, request);
        return ResponseEntity.noContent().build();
    }

    /* 차량 삭제 */
    @DeleteMapping("/{carId}")
    public ResponseEntity<Void> deleteCar(@PathVariable Long carId) {
        carService.deleteCar(carId);
        return ResponseEntity.noContent().build();
    }

    /* 전체 차량 목록 조회 */
    @GetMapping
    public ResponseEntity<Page<CarListResponse>> getCarList(
            @PageableDefault(size = 20, sort = "id") Pageable pageable
    ) {
        return ResponseEntity.ok(carService.getCarList(pageable));
    }

    /* 차량 기본 정보 조회(상세조회) */
    @GetMapping("/{carId}")
    public ResponseEntity<CarDetailResponse> getCarDetail(@PathVariable Long carId) {
        return ResponseEntity.ok(carService.getCarDetail(carId));
    }

}