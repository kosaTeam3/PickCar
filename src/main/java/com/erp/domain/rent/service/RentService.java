package com.erp.domain.rent.service;

import com.erp.domain.car.entity.Car;
import com.erp.domain.car.repository.CarRepository;
import com.erp.domain.client.entity.Client;
import com.erp.domain.client.repository.ClientRepository;
import com.erp.domain.coupon.entity.ClientCoupon;
import com.erp.domain.coupon.entity.Coupon;
import com.erp.domain.coupon.repository.ClientCouponRepository;
import com.erp.domain.rent.dto.request.RentCreateRequest;
import com.erp.domain.rent.dto.response.CurrentRentResponse;
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

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RentService {

    private final RentRepository rentRepository;
    private final CarRepository carRepository;
    private final ClientRepository clientRepository;
    private final RentalFeeService rentalFeeService;
    private final ClientCouponRepository clientCouponRepository;

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
    public RentCreateResponse createRent(RentCreateRequest request, Long userId) {
        // 차량 및 고객 조회
        Car car = carRepository.findById(request.carId())
                .orElseThrow(() -> new CustomException(404, "차량을 찾을 수 없습니다."));

        Client client = clientRepository.findById(userId)
                .orElseThrow(() -> new CustomException(404, "고객을 찾을 수 없습니다."));

        // 해당 기간에 이미 예약이 있는지 확인. 1차 검증
        boolean isOverlapped = rentRepository.existsOverlappingRent(
                request.carId(), request.startRentDateTime(), request.endRentDateTime()
        );

        if (isOverlapped) {
            throw new CustomException(409, "선택하신 시간대에 이미 예약된 차량입니다.");
        }

        long totalHours = rentalFeeService.calculateRentalHours(request.startRentDateTime(), request.endRentDateTime());

        long rentalFee = rentalFeeService.calculateRentalFee(car, totalHours);

        Long appliedClientCouponId = null;

        if (request.clientCouponId() != null) {
            ClientCoupon clientCoupon = clientCouponRepository.findById(request.clientCouponId())
                    .orElseThrow(() -> new CustomException(404, "보유하신 쿠폰 정보를 찾을 수 없습니다."));

            // 소유권 검증 (내 쿠폰이 맞는지)
            if (!clientCoupon.getClient().getId().equals(userId)) {
                throw new CustomException(403, "해당 쿠폰에 대한 권한이 없습니다.");
            }

            // 사용 여부 검증
            if (clientCoupon.isUsed()) {
                throw new CustomException(400, "이미 사용된 쿠폰입니다.");
            }

            Coupon coupon = clientCoupon.getCoupon();

            // 유효 기간 검증
            if (coupon.getExpDate().isBefore(LocalDate.now())) {
                throw new CustomException(400, "기한이 만료된 쿠폰입니다.");
            }

            // 할인 적용
            rentalFee -= coupon.getDiscount();

            // 적용된 ClientCoupon ID 저장 준비
            appliedClientCouponId = clientCoupon.getId();
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
                .clientCouponId(appliedClientCouponId)
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

    public CurrentRentResponse findCurrentRent(Long userId) {
        // 현재 시간 기준, 예약 확정(RESERVED) 상태인 렌트 정보 조회
        Rent rent = rentRepository.findCurrentRentByClient(userId, LocalDateTime.now(), RentStatus.RESERVED)
                .orElseThrow(() -> new CustomException(400, "현재 렌트중인 차량이 없습니다."));

        return CurrentRentResponse.builder()
                .rentId(rent.getId())
                .carId(rent.getCar().getId())
                .carImage(rent.getCar().getImage())
                .carModel(rent.getCar().getModel())
                .rentId(rent.getId())
                .rentalFee(rent.getRentalFee())
                .carBrand(rent.getCar().getBrand())
                .carYear(rent.getCar().getYear())
                .startRentDateTime(rent.getStartRentDateTime())
                .endRentDateTime(rent.getEndRentDateTime())
                .branchName(rent.getCar().getBranch().getName())
                .branchAddress(rent.getCar().getBranch().getAddress())
                .build();
    }
}
