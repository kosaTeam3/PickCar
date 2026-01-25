package com.erp.domain.rent.service;

import com.erp.domain.car.entity.Car;
import com.erp.domain.car.entity.CarStatus;
import com.erp.domain.car.repository.CarRepository;
import com.erp.domain.client.entity.Client;
import com.erp.domain.client.repository.ClientRepository;
import com.erp.domain.coupon.entity.ClientCoupon;
import com.erp.domain.coupon.entity.Coupon;
import com.erp.domain.coupon.repository.ClientCouponRepository;
import com.erp.domain.rent.dto.request.RentCreateRequest;
import com.erp.domain.rent.dto.request.RentManagerSearchRequest;
import com.erp.domain.rent.dto.response.*;
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
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RentService {

    private final RentRepository rentRepository;
    private final CarRepository carRepository;
    private final ClientRepository clientRepository;
    private final RentalFeeService rentalFeeService;
    private final ClientCouponRepository clientCouponRepository;

    // 최대 대여 기간을 상수로 정의 (14일 * 24시간 = 336시간)
    private static final int MAX_RENTAL_DAYS = 14;
    private static final int HOURS_IN_A_DAY = 24;

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
        // 고객의 기존 활성 예약 여부 검증
        if (rentRepository.existsActiveRentByClientId(userId)) {
            throw new CustomException(400, "이미 진행 중이거나 예약된 대여가 존재합니다. 1인당 1건의 예약만 가능합니다.");
        }

        // 차량 및 고객 조회
        Car car = carRepository.findById(request.carId())
                .orElseThrow(() -> new CustomException(404, "차량을 찾을 수 없습니다."));

        Client client = clientRepository.findById(userId)
                .orElseThrow(() -> new CustomException(404, "고객을 찾을 수 없습니다."));

        // 블랙리스트 여부 검증
        if (client.getBlacklisted()) {
            throw new CustomException(403, "블랙리스트에 등록된 회원은 차량 대여가 불가능하다.");
        }

        // 대여 기간 계산
        long totalHours = rentalFeeService.calculateRentalHours(request.startRentDateTime(), request.endRentDateTime());

        // 대여 기간이 14일을 초과하는지 검증
        if (totalHours > (long) MAX_RENTAL_DAYS * HOURS_IN_A_DAY) {
            throw new CustomException(400, "대여 기간은 최대 14일을 초과할 수 없습니다.");
        }

        // 내 기존 결제 대기 내역(WAITING_PAYMENT)이 있다면 삭제 (재결제 시도 허용)
        List<Rent> myWaitingRents = rentRepository.findMyWaitingRents(
                request.carId(),
                client.getId(),
                request.startRentDateTime(),
                request.endRentDateTime()
        );

        if (!myWaitingRents.isEmpty()) {
            rentRepository.deleteAll(myWaitingRents);
            rentRepository.flush(); // 즉시 삭제 반영
        }

        // 해당 기간에 이미 예약이 있는지 확인. 1차 검증
        boolean isOverlapped = rentRepository.existsOverlappingRent(
                request.carId(), request.startRentDateTime(), request.endRentDateTime()
        );

        if (isOverlapped) {
            throw new CustomException(409, "선택하신 시간대에 이미 예약된 차량입니다.");
        }

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

        // 주문 번호 (merchantUid), 예: RENT_20260210_101_1706182000
        String merchantUid = "RENT_" + LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE) + "_"
                + savedRent.getId() + "_" + System.currentTimeMillis();

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
        // 예약 확정(RESERVED) 상태인 렌트 정보 조회
        Rent rent = rentRepository.findCurrentRentByClient(userId, RentStatus.RESERVED)
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

    @Transactional
    public RentReturnResponse returnRental(Long rentId) {
        // 예약 조회
        Rent rent = rentRepository.findById(rentId)
                .orElseThrow(() -> new CustomException(404, "예약 정보를 찾을 수 없습니다."));

        // 상태 검증 (이미 반납되었거나 취소된 예약인지)
        if (rent.getStatus() != RentStatus.RESERVED) {
            throw new CustomException(400, "반납 가능한 상태가 아닙니다. (현재 상태: " + rent.getStatus() + ")");
        }

        // Rent 상태 변경 (RESERVED -> COMPLETED)
        rent.setStatus(RentStatus.COMPLETED);

        // Car 상태 변경 (DRIVING -> WAITING)
        Car car = rent.getCar();
        car.setStatus(CarStatus.WAITING);

        return RentReturnResponse.builder()
                .rentId(rent.getId())
                .rentStatus(rent.getStatus())
                .returnDateTime(LocalDateTime.now())
                .message("차량 반납이 완료되었습니다.")
                .build();
    }

    // 예약 시간 전 대여 가능 버퍼 시간
    private static final long PICKUP_BUFFER_MINUTES = 15;

    @Transactional
    public void startRental(Long rentId) {
        Rent rent = rentRepository.findById(rentId)
                .orElseThrow(() -> new CustomException(404, "예약 정보를 찾을 수 없습니다."));

        // RESERVED 상태인지 확인
        if (rent.getStatus() != RentStatus.RESERVED) {
            throw new CustomException(400, "대여 시작이 가능한 상태가 아닙니다.");
        }


        // // 현재 시각과 예약 시작 시각 비교 (버퍼 적용)
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime earliestAvailableTime = rent.getStartRentDateTime().minusMinutes(PICKUP_BUFFER_MINUTES);

        if (now.isBefore(earliestAvailableTime)) {
            throw new CustomException(400, "아직 대여 인도 가능 시간이 아닙니다. 예약 시간 15분 전부터 처리가 가능합니다.");
        }

        // 차량 상태를 운행 중(DRIVING)으로 변경
        rent.getCar().setStatus(CarStatus.DRIVING);
    }

    public Page<RentHistListResponse>
    getRentHistories(Long userId, Pageable pageRequest) {
        return rentRepository.findRentHistories(userId, pageRequest)
                .map(data -> RentHistListResponse.builder()
                        .carId(data.getCarId())
                        .stringImage(data.getStringImage())
                        .model(data.getModel())
                        .brand(data.getBrand())
                        .year(data.getYear())
                        .status(data.getStatus())
                        .branchId(data.getBranchId())
                        .startRentDateTime(data.getStartRentDateTime())
                        .endRentDateTime(data.getEndRentDateTime())
                        .build());

    }

    public Page<RentManagerListResponse> getManagerRentals(RentManagerSearchRequest request, Pageable pageable) {
        String status = request.status();
        CarStatus targetCarStatus;

        // status 파라미터 유효성 검증 및 변환
        if ("pickup-waiting".equals(status)) {
            targetCarStatus = CarStatus.WAITING; // 인도 대기
        } else if ("return-waiting".equals(status)) {
            targetCarStatus = CarStatus.DRIVING; // 인수 대기
        } else {
            throw new CustomException(400, "잘못된 조회 상태 값입니다. (pickup-waiting 또는 return-waiting 만 가능)");
        }

        Page<Rent> rents = rentRepository.findManagerRentals(
                RentStatus.RESERVED,
                targetCarStatus,
                request.branchId(),
                request.carNumber(),
                request.clientName(),
                pageable
        );

        return rents.map(rent -> RentManagerListResponse.builder()
                .rentId(rent.getId())
                .carId(rent.getCar().getId())
                .carNumber(rent.getCar().getCarNumber())
                .model(rent.getCar().getModel())
                .clientName(rent.getClient().getName())
                .clientPhone(rent.getClient().getPhoneNumber())
                .carStatus(rent.getCar().getStatus())
                .startRentDateTime(rent.getStartRentDateTime())
                .endRentDateTime(rent.getEndRentDateTime())
                .build());
    }
}
