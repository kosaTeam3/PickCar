# 🛻프로젝트 소개
![LOGO](https://github.com/kosaTeam3/PickCar/blob/dev/20260203_223426.png)

### Spring Boot 기반의 공유차량 통합운영관리 및 예약 플랫폼

<br/>

# 📅 개발 기간
- **26.01.07 ~ 26.01.25**

<br/>

# 👨‍👩‍👧‍👦 팀원 소개
> ### 💳 **[강화민](https://github.com/hamin-kang)**
> - **역할:** Backend Developer
> - **담당업무:** 공유차량 예약 및 결제 시스템 구현, 차량 사고 관리 및 처리 프로세스, 프로젝트 전반 일정 및 이슈 관리

---

> ### ⚙️ **[권재현](https://github.com/Galmaeki)**
> - **역할:** Backend Developer / Frontend Developer
> - **담당업무:** 프론트엔드 아키텍처 및 공통 컴포넌트, 마이페이지, 지점 관리 및 코드 리뷰, 백엔드 코드 품질 관리 및 최적화

---

> ### 📊 **[김혜진](https://github.com/kimhyejin1030)**
> - **역할:** Backend Developer 
> - **담당업무:** 통계 데이터 시각화 및 분석, 쿠폰 발급 및 관리 시스템, 차량 정보 등록 및 현황 관리

---

> ### 👨‍🔧 **[이대승](https://github.com/bigwin0207)**
> - **역할:** Backend Developer  
> - **담당업무:** 차량 정비 일정 및 이력 관리, 회원 관리 및 정보 수정, 실시간 알림 서비스 구현

---

> ### 🔐 **[조재표](https://github.com/berichmore)**
> - **역할:** Backend Developer
> - **담당업무:** JWT 기반 로그인/인증 구현, 회원가입 및 권한 관리 프로세스, 관리자 권한 직원 등록 기능

---

<br/>

# Tech Stack
![Tech Stack](https://github.com/kosaTeam3/PickCar/blob/dev/20260203_223356.png)

<br/>

### Backend 

- **Java 21:** 최신 LTS 버전의 강력한 성능과 기능을 갖춘 언어 환경

- **Spring Boot 4.0.1:** 최신 버전의 안정적이고 생산성 높은 애플리케이션 프레임워크

- **Spring Data JPA:** 인터페이스 기반의 객체 지향적 데이터 접근 및 관리

- **Spring Security:** 사용자 인증 및 권한 부여를 위한 보안 프레임워크

- **JWT:** 무상태(Stateless) 기반의 안전한 사용자 인증 시스템 구현

- **Lombok:** 반복되는 코드 자동화를 통한 생산성 및 가독성 향상

- **Gradle:** 효율적인 빌드 자동화 및 라이브러리 의존성 관리

### Database
**MySQL:** 데이터 정합성과 신뢰성을 보장하는 오픈소스 RDBMS

<br/>

# [ERD](https://www.erdcloud.com/d/2TqPzooJrKLYrFMn6) 

# ![ERD](https://github.com/kosaTeam3/PickCar/blob/dev/image.png)

#### 1. 고객 예약 및 결제 관계 (Client ↔ Rent ↔ Payment)

- **Client(고객) & Rent(예약):** 1:N 관계입니다. 한 명의 고객은 여러 번 차량을 예약할 수 있습니다.

- **Rent(예약) & Payment(결제):** 1:1 관계에 가깝습니다. 특정 예약(rent_id)에 대해 하나의 결제 정보가 생성됩니다. Payment 테이블은 imp_uid(아임포트 ID)를 통해 외부 결제 연동 상태를 관리합니다.

- **Client(고객) & Coupon(쿠폰):** N:M 관계입니다. 이를 해결하기 위해 Client_Coupon이라는 중간 테이블이 존재합니다. 고객은 여러 쿠폰을 가질 수 있고, 하나의 쿠폰 정보는 여러 고객에게 발급될 수 있습니다.

#### 2. 차량 및 지점 관리 관계 (Branch ↔ Car)

- **Branch(지점) & Car(차량):** 1:N 관계입니다. 모든 차량은 반드시 하나의 지점(branch_id)에 소속되어야 합니다.

- **Branch(지점) & Employee(직원):** 1:N 관계입니다. 한 지점에는 여러 직원이 근무하며, Branch 테이블의 manager_id는 해당 지점을 관리하는 특정 직원(Employee)을 가리킵니다.

#### 3. 정비 및 사고 이력 관계 (Car ↔ Maintenance / Accident)

- **Car(차량) & Maintenance(정비):** 1:N 관계입니다. 한 차량은 시간에 따라 여러 번의 정비(Maintenance) 기록을 가집니다. 이때 어떤 직원(employee_id)이 정비를 담당했는지 기록하여 책임 소재를 명확히 합니다.

- **Car(차량) & Accident(사고):** 1:N 관계입니다. 차량 운행 중 발생한 사고를 기록합니다.

- **Rent(예약) & Accident(사고):** 특정 예약 건(rent_id)과 연결되어, 어떤 고객이 이용 중에 사고가 났는지 추적할 수 있습니다.

#### 4. 조직 및 알림 관계 (Employee ↔ Alert / Notice)

- **Employee(직원) & Alert(알림):** 1:N 관계입니다. 특정 직원(employee_id)에게 발생하는 개인적인 업무 알림을 관리합니다.

- **Branch(지점) & Notice(공지):** 1:N 관계입니다. 공지사항은 전체 대상일 수도 있지만, 특정 지점(branch_id)에만 해당하는 공지일 수도 있도록 설계되어 있습니다.

