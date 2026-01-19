package com.erp.domain.accident.service;

import com.erp.domain.accident.dto.request.AccidentRequest;
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

    @Transactional
    public void updateAccident(Long accidentId, AccidentRequest request) {
        Accident accident = accidentRepository.findById(accidentId)
                .orElseThrow(() -> new CustomException(404, "해당 사고 정보를 찾을 수 없습니다."));

        if (request.status() != null) {
            accident.setStatus(request.status());
        }
        if (request.description() != null) {
            accident.setDescription(request.description());
        }
        if (request.location() != null) {
            accident.setLocation(request.location());
        }
        if (request.time() != null) {
            accident.setTime(request.time());
        }
        if (request.part() != null) {
            accident.setPart(request.part());
        }
        if (request.repairCost() != null) {
            accident.setRepairCost(request.repairCost());
        }
        if (request.clientLiability() != null) {
            accident.setClientLiability(request.clientLiability());
        }
    }
}
