package com.erp.domain.rent.service;

import com.erp.domain.rent.dto.response.RentHistoryResponse;
import com.erp.domain.rent.entity.Rent;
import com.erp.domain.rent.repository.RentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RentService {

    private final RentRepository rentRepository;

    public Page<RentHistoryResponse> getRentHistory(Long carId, Pageable pageable) {

        Page<Rent> rents = rentRepository.findRentHistoryByCarId(carId, LocalDateTime.now(), pageable);

        return rents.map(rent -> new RentHistoryResponse(
                rent.getId(),
                rent.getClient().getName(),
                rent.getClient().getPhoneNumber(),
                rent.getStartRentDateTime(),
                rent.getEndRentDateTime()
        ));
    }
}
