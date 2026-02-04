# 🚗 PickCar: 공유차량 통합 운영 관리 및 예약 플랫폼

**PickCar**는 서울 시내 6개 주요 거점을 중심으로 운영되는 공유 차량 플랫폼이다. 일반 사용자를 위한 **B2C 예약 서비스**와 본사 및 지점 운영을 위한 **통합 운영 관리 시스템(Back-office)** 을 이원화하여 제공한다.

---

## 1. 프로젝트 개요

* **프로젝트명**: PickCar
* **개발 기간**: 2026.01.05 ~ 2026.01.27
* **주요 목표**: 실시간 차량 예약, 지점별 자산 관리, 정교한 요금 정책 및 사고/정비 이력 관리의 통합화.

---

## 2. 기술 스택

### Backend

* **Language**: Java 21
* **Framework**: Spring Boot 4.0.1
* **ORM**: Spring Data JPA
* **Security**: Spring Security, JWT (Json Web Token)
* **Database**: MySQL
* **Build Tool**: Gradle

### Frontend

* **Library**: React (PickCarFront 프로젝트)
* **Style**: Tailwind CSS


---

## 3. 핵심 기능

### 🏢 통합 운영 관리 시스템 (Back-office)

* **직원 계정 거버넌스**: 관리자가 직원을 직접 등록하며, 특정 알고리즘(`PC`+`입사연도`+`지점코드`+`순번`)에 따른 사번 및 비밀번호를 부여한다.
* **차량 및 자산 관리**: 차량의 생애주기(등록~폐차) 및 실시간 상태를 관리하며, 주행 거리에 따른 11종 소모품 교체 알림 기능을 제공한다.
* **사고 및 정비 관리**: 현장 직원이 실시간으로 차량 상태와 정비 기록을 업데이트하며, 사고 발생 시 렌트 상태와 연동하여 사고 보고를 처리한다.
* **통계**: 지점별 매출 통계, 차량 가동률, 인기 차종 분석 대시보드를 제공한다.

### 📱 B2C 예약 클라이언트

* **GPS 기반 지점 찾기**: 사용자 위치에서 가장 가까운 6개 거점 지점을 거리순으로 정렬하여 제공한다.
* **실시간 예약 및 결제**: 동시성 제어를 통해 중복 예약을 방지하며, PortOne API를 연동한 결제 프로세스를 지원한다.
* **요금 정책**: 시간당 대여료, 보험료, 주행료를 합산한 요금 체계를 적용하며 10시간 이상 대여 시 '일일 상한제'를 자동 적용한다.
* **마이페이지**: 현재 이용 중인 차량 정보를 최상단에 노출하고, 과거 이용 내역 및 쿠폰 관리를 지원한다.

---

## 4. 역할 분담

PickCar 프로젝트는 기능별 도메인에 따라 다음과 같이 역할을 분담하여 개발하였다.

* **[👑 강화민(팀장)](https://github.com/hamin-kang)**: 공유차량 예약 및 결제 시스템, 차량 사고 관리
* **[💻 권재현(부팀장)](https://github.com/Galmaeki)**: 프론트 전반, 마이페이지, 지점 관리, 코드 관리
* **[📈 김혜진](https://github.com/kimhyejin1030)**: 통계, 쿠폰 시스템, 차량 관리
* **[🛠️ 이대승](https://github.com/bigwin0207)**: 정비, 회원 관리 및 알림
* **[🛡️ 조재표](https://github.com/berichmore)**: 로그인/회원가입, 직원 등록

---

## 5. ERD (Entity-Relationship Diagram)
- https://www.erdcloud.com/d/2TqPzooJrKLYrFMn6

[![ERD Diagram](image.png)](https://www.erdcloud.com/d/2TqPzooJrKLYrFMn6)