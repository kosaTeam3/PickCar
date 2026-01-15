package com.erp.domain.car.controller;

import com.erp.domain.car.dto.request.CarCreateRequest;
import com.erp.domain.car.dto.request.CarSearchRequest;
import com.erp.domain.car.dto.request.CarUpdateRequest;
import com.erp.domain.car.dto.response.CarDetailResponse;
import com.erp.domain.car.dto.response.CarListResponse;
import com.erp.domain.car.dto.response.CarMaintenanceAlertResponse;
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
    public Long createCar(@RequestBody CarCreateRequest request) {
        return carService.createCar(request);
    }

    /* 차량 수정 */
    @PatchMapping("/{carId}")
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

    /* 전체 차량 목록 조회 + 조건 검색 통합 */
    @GetMapping
    public Page<CarListResponse> getCarList(
            @ModelAttribute CarSearchRequest request,
            @PageableDefault(size = 20, sort = "id") Pageable pageable
    ) {
        return carService.searchCars(request, pageable);
    }

    /* 차량 기본 정보 조회(상세조회) */
    @GetMapping("/{carId}")
    public CarDetailResponse getCarDetail(@PathVariable Long carId) {
        return carService.getCarDetail(carId);
    }

    /* 차량 정비 알림 조회 */
    @GetMapping("/{carId}/maintenanceAlerts")
    public CarMaintenanceAlertResponse getMaintenanceAlerts(@PathVariable Long carId) {
        return carService.getMaintenanceAlerts(carId);
    }
}