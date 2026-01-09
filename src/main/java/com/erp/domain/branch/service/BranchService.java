package com.erp.domain.branch.service;

import com.erp.domain.branch.dto.request.BranchSearchRequest;
import com.erp.domain.branch.dto.response.BranchResponse;
import com.erp.domain.branch.repository.BranchRepository;
import com.erp.domain.branch.repository.BranchWithDistance;
import com.erp.domain.car.entity.CarStatus;
import com.erp.domain.car.repository.CarRepository;
import com.erp.domain.rent.repository.RentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BranchService {
    private final BranchRepository branchRepository;
    private final CarRepository carRepository;
    private final RentRepository rentRepository;

    public List<BranchResponse> getAvailableBranches(BranchSearchRequest request) {
        // Time Parsing
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime startRentDateTime = LocalDateTime.parse(request.startRentDateTime(), formatter);
        LocalDateTime endRentDateTime = LocalDateTime.parse(request.endRentDateTime(), formatter);

        // 거리순(ASC) 지점 목록 조회
        List<BranchWithDistance> branches = branchRepository.findBranchesByDistance(
                request.userLatitude(), request.userLongitude());

        // 해당 기간 내 이미 예약된 차량 ID 목록 조회
        List<Long> rentedCarIds = rentRepository.findRentedCarIds(startRentDateTime, endRentDateTime);

        // 각 지점별로 가용 차량 계산
        return branches.stream()
                .map(branch -> {
                    int availableVehicles;

                    // 예약된 차량 리스트가 비어있으면 전체 카운트, 있으면 제외 카운트 호출
                    if (rentedCarIds.isEmpty()) {
                        availableVehicles = carRepository.countByBranchIdAndStatus(
                                branch.getId(), CarStatus.WAITING);
                    } else {
                        availableVehicles = carRepository.countAvailableCarsNotIn(
                                branch.getId(), CarStatus.WAITING, rentedCarIds);
                    }

                    return new BranchResponse(
                            branch.getId(),
                            branch.getName(),
                            availableVehicles,
                            branch.getLatitude(),
                            branch.getLongitude(),
                            branch.getDistance()
                    );
                })
                .toList();
    }
}
