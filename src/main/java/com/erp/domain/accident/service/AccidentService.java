package com.erp.domain.accident.service;

import com.erp.domain.accident.dto.request.AccidentRequest;
import com.erp.domain.accident.dto.response.AccidentDetailResponse;
import com.erp.domain.accident.entity.Accident;
import com.erp.domain.accident.repository.AccidentRepository;
import com.erp.domain.car.entity.Car;
import com.erp.domain.car.entity.CarStatus;
import com.erp.domain.car.repository.CarRepository;
import com.erp.domain.rent.entity.Rent;
import com.erp.domain.rent.repository.RentRepository;
import com.erp.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AccidentService {
    private final AccidentRepository accidentRepository;
    private final CarRepository carRepository;
    private final RentRepository rentRepository;

    @Transactional
    public Long createAccident(Long carId, AccidentRequest request) {
        // 차량 조회 (사고 등록의 주체)
        Car car = carRepository.findById(carId)
                .orElseThrow(() -> new CustomException(404, "해당 차량을 찾을 수 없습니다."));

        // 현재 시점의 차량 대여 정보 조회 (있으면 가져오고 없으면 null)
        Rent rent = rentRepository.findCurrentRent(carId, LocalDateTime.now())
                .orElse(null);

        Accident accident = Accident.builder()
                .rent(rent) // null 허용
                .car(car)
                .status(request.status())
                .description(request.description())
                .location(request.location())
                .time(request.time())
                .part(request.part())
                .repairCost(request.repairCost())
                .clientLiability(request.clientLiability())
                .build();

        // 차량 상태 변경
        car.setStatus(CarStatus.MAINTENANCE);

        return accidentRepository.save(accident).getId();
    }

    public AccidentDetailResponse getAccidentDetail(Long accidentId) {
        Accident accident = accidentRepository.findById(accidentId)
                .orElseThrow(() -> new CustomException(404, "해당 사고 정보를 찾을 수 없습니다."));

        Rent rent = accident.getRent();
        Long clientId = null;
        String clientName = null;

        // Rent 정보가 있을 경우에만 고객 정보 추출
        if (rent != null) {
            clientId = rent.getClient().getId();
            clientName = rent.getClient().getName();
        }

        return new AccidentDetailResponse(
                accident.getId(),
                accident.getStatus(),
                accident.getDescription(),
                accident.getLocation(),
                accident.getTime(),
                accident.getPart(),
                accident.getRepairCost(),
                accident.getClientLiability(),
                clientId,
                clientName,
                accident.getCar().getId(),
                accident.getCar().getInsuranceName(),
                accident.getCar().getVehicleIdNumber(),
                accident.getCar().getBrand(),
                accident.getCar().getModel(),
                accident.getCar().getYear()
        );
    }
}
