package com.erp.domain.rent.service;

import com.erp.domain.car.entity.Car;
import com.erp.domain.car.repository.CarRepository;
import com.erp.domain.client.entity.Client;
import com.erp.domain.client.repository.ClientRepository;
import com.erp.domain.coupon.entity.Coupon;
import com.erp.domain.coupon.repository.CouponRepository;
import com.erp.domain.rent.dto.request.RentCreateRequest;
import com.erp.domain.rent.dto.response.RentCreateResponse;
import com.erp.domain.rent.dto.response.RentHistoryResponse;
import com.erp.domain.rent.entity.Rent;
import com.erp.domain.rent.entity.RentStatus;
import com.erp.domain.rent.repository.RentRepository;
import com.erp.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RentService {

    private final RentRepository rentRepository;
    private final CarRepository carRepository;
    private final ClientRepository clientRepository;
    private final CouponRepository couponRepository;

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

    @Transactional
    public RentCreateResponse createRent(RentCreateRequest request, Long clientId) {
        // 차량 및 고객 조회
        Car car = carRepository.findById(request.carId())
                .orElseThrow(() -> new CustomException(404, "차량을 찾을 수 없습니다."));

        Client client = clientRepository.findById(clientId)
                .orElseThrow(() -> new CustomException(404, "고객을 찾을 수 없습니다."));

        // 해당 기간에 이미 예약이 있는지 확인. 1차 검증
        boolean isOverlapped = rentRepository.existsOverlappingRent(
                request.carId(), request.startRentDateTime(), request.endRentDateTime()
        );

        if (isOverlapped) {
            throw new CustomException(409, "선택하신 시간대에 이미 예약된 차량입니다.");
        }

        // 렌트 요금 계산
        // 1분이라도 초과되면 1시간으로 올림 처리
        long minutes = ChronoUnit.MINUTES.between(request.startRentDateTime(), request.endRentDateTime());
        long totalHours = (long) Math.ceil(minutes / 60.0);

        long rentalFee = car.calculateRentalFee(totalHours);

        // 쿠폰 적용
        if (request.couponId() != null) {
            Coupon coupon = couponRepository.findById(request.couponId())
                    .orElseThrow(() -> new CustomException(404, "쿠폰을 찾을 수 없습니다."));

            // 쿠폰 유효성 검증
            if (coupon.getExpDate().isBefore(LocalDate.now())) {
                throw new CustomException(400, "기한이 만료된 쿠폰입니다.");
            }

            // 정액 할인 적용
            rentalFee -= coupon.getDiscount();
        }

        if (rentalFee < 0) rentalFee = 0;

        // Rent 엔티티 생성 및 저장 (WAITING_PAYMENT)
        Rent rent = Rent.builder()
                .car(car)
                .client(client)
                .startRentDateTime(request.startRentDateTime())
                .endRentDateTime(request.endRentDateTime())
                .rentalFee(rentalFee)
                .status(RentStatus.WAITING_PAYMENT)
                .build();

        Rent savedRent = rentRepository.save(rent);

        // 주문 번호 (merchantUid), 예: RENT_20260210_101
        String merchantUid = "RENT_" + LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE) + "_" + savedRent.getId();

        return RentCreateResponse.builder()
                .rentId(savedRent.getId())
                .merchantUid(merchantUid)
                .amount(savedRent.getRentalFee())
                .carModel(car.getModel())
                .buyerName(client.getName())
                .buyerEmail(client.getEmail())
                .buyerPhone(client.getPhoneNumber())
                .build();
    }
}
