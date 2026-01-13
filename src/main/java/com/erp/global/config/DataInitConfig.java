package com.erp.global.config;

import com.erp.domain.accident.entity.Accident;
import com.erp.domain.accident.entity.AccidentStatus;
import com.erp.domain.accident.repository.AccidentRepository;
import com.erp.domain.branch.entity.Branch;
import com.erp.domain.branch.repository.BranchRepository;
import com.erp.domain.car.entity.Car;
import com.erp.domain.car.entity.CarColor;
import com.erp.domain.car.entity.CarStatus;
import com.erp.domain.car.entity.FuelType;
import com.erp.domain.car.repository.CarRepository;
import com.erp.domain.client.entity.Client;
import com.erp.domain.client.entity.Gender;
import com.erp.domain.client.repository.ClientRepository;
import com.erp.domain.employee.entity.Employee;
import com.erp.domain.employee.entity.EmployeeAuthority;
import com.erp.domain.employee.repository.EmployeeRepository;
import com.erp.domain.maintenance.entity.Maintenance;
import com.erp.domain.maintenance.entity.MaintenanceStatus;
import com.erp.domain.maintenance.repository.MaintenanceRepository;
import com.erp.domain.rent.entity.Rent;
import com.erp.domain.rent.repository.RentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Configuration
@RequiredArgsConstructor // Repository 자동 주입을 위해 추가
public class DataInitConfig {
    @Bean
    public CommandLineRunner initData(
            ClientRepository clientRepository,
            AccidentRepository accidentRepository,
            RentRepository rentRepository,
            CarRepository carRepository,
            BranchRepository branchRepository,
            EmployeeRepository employeeRepository,
            MaintenanceRepository maintenanceRepository) {
        return args -> {
            if (clientRepository.count() > 0) return;

            // 1. 지점 생성
            Branch mainBranch = branchRepository.save(Branch.builder()
                    .name("서울 강남 본점")
                    .phoneNumber("02-123-4567")
                    .address("서울시 강남구 테헤란로 123")
                    .latitude(37.4979)
                    .longitude(127.0276)
                    .employeeCount(10)
                    .carCount(50)
                    .build());

            // 2. 회원 생성 (hong.getId() 에러가 났던 이유는 필드명이 clientId(객체)이기 때문입니다)
            Client hong = clientRepository.save(Client.builder()
                    .email("test1@naver.com")
                    .name("홍길동")
                    .birthday(LocalDate.of(1995, 5, 20))
                    .gender(Gender.MALE)
                    .licenceNumber("11-22-333333-44")
                    .licenceArea("서울")
                    .licenceDay(LocalDate.of(2020, 1, 1))
                    .password("1234")
                    .blacklisted(false)
                    .phoneNumber("010-1234-5678")
                    .build());

            // 3. 자동차 생성
            Car grandeur = carRepository.save(Car.builder()
                    .branch(mainBranch)
                    .vehicleIdNumber("VIN-GR-2024")
                    .model("그랜저 GN7")
                    .price(45000000L)
                    .brand("Hyundai")
                    .year(2024)
                    .ageLimit(26)
                    .fuelType(FuelType.GASOLINE)
                    .carNumber("123가 4567")
                    .mileage(5000L)
                    .status(CarStatus.DRIVING)
                    .purchasePrice(40000000L)
                    .modelPrice(42000000L)
                    .seater(5)
                    .color(CarColor.BLACK)
                    .build());

            Car tesla = carRepository.save(Car.builder()
                    .branch(mainBranch)
                    .vehicleIdNumber("VIN-GR-2024")
                    .model("테슬라 GN7")
                    .price(45000000L)
                    .brand("테슬라")
                    .year(2024)
                    .ageLimit(26)
                    .fuelType(FuelType.GASOLINE)
                    .carNumber("123가 4567")
                    .mileage(5000L)
                    .status(CarStatus.DRIVING)
                    .purchasePrice(40000000L)
                    .modelPrice(42000000L)
                    .seater(5)
                    .color(CarColor.BLACK)
                    .build());

            // 4. 사고 기록 저장
            accidentRepository.save(Accident.builder()
                    .client(hong)
                    .car(grandeur)
                    .accidentTime(LocalDateTime.now().minusMonths(1))
                    .accidentStatus(AccidentStatus.COMPLETED)
                    .accidentDetail("주차 중 휀다 긁힘 사고")
                    .accidentLocate("서울 강남구")
                    .accidentPart("조수석 앞 휀다")
                    .insuranceInfo("ㅇㄴㅁ")
                    .brand("현대")
                    .model("그랜저")
                    .year("2024")
                    .build());

            // 5. 렌트 기록 저장 (Rent 엔티티의 모든 필수 필드 포함)
            rentRepository.save(Rent.builder()
                    .clientId(hong)       // 필드명은 clientId지만 타입은 Client 객체임
                    .car(grandeur)        // car_id가 null이 되지 않도록 객체 주입
                    .carName("그랜저 GN7")
                    .model("Premium Selection")
                    .brand("Hyundai")
                    .price("85,000원")
                    .year(2024)           // 필수 (Integer)
                    .fuelType(FuelType.GASOLINE) // 필수 (Enum)
                    .ageLimit("만 26세 이상") // 필수 (String)
                    .seater("5인승")       // 필수 (String)
                    .color("Midnight Black") // 필수 (String)
                    .startRentDateTime(LocalDateTime.now().minusDays(3))
                    .endRentDateTime(LocalDateTime.now().minusDays(1))
                    .build());

            System.out.println(">>> [성공] 데이터 초기화 완료! 홍길동 ID: " + hong.getId());

            // 1. 담당 직원 생성 (정비 등록을 위해 필요)
            Employee mechanic = employeeRepository.save(Employee.builder()
                    .branch(mainBranch)
                    .name("김정비")
                    .email("mechanic@pickcar.com")
                    .password("1234")
                    .grade("등급?")
                    .authority(EmployeeAuthority.STAFF)
                    .entryDate(LocalDate.now())
                    .phoneNumber("010-9999-8888")
                    .loginId("id")
                    .password("1234")
                    .build());

// 2. 정비 데이터 1 (완료된 정비 - 전체 조회/상세 조회 테스트용)
            maintenanceRepository.save(Maintenance.builder()
                    .branch(mainBranch)
                    .car(grandeur)
                    .employee(mechanic)
                    .employeeName(mechanic.getName())
                    .vehicleIdNumber(grandeur.getVehicleIdNumber())
                    .title("엔진오일 및 필터 교체")
                    .maintenanceDate(LocalDate.now().minusDays(5))
                    .cost(120000L)
                    .status(MaintenanceStatus.COMPLETED)
                    .detail("정기 점검 및 소모품 교체 완료")
                    .build());

// 3. 정비 데이터 2 (대기 중인 정비 - 수정/삭제/검색 테스트용)
            maintenanceRepository.save(Maintenance.builder()
                    .branch(mainBranch)
                    .car(tesla)
                    .employee(mechanic)
                    .employeeName(mechanic.getName())
                    .vehicleIdNumber(grandeur.getVehicleIdNumber())
                    .title("타이어 공기압 점검")
                    .maintenanceDate(LocalDate.now())
                    .cost(0L)
                    .status(MaintenanceStatus.SCHEDULE)
                    .detail("왼쪽 뒷바퀴 공기압 미세 누출 의심")
                    .build());

            System.out.println(">>> [성공] 정비(Maintenance) 테스트 데이터 초기화 완료!");
        };
    }
}