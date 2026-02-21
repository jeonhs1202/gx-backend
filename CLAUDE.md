# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## 프로젝트 개요

헬스장 GX(Group Exercise) 예약 · 회원권 차감 · 출결 · 패널티 · 보너스 · 알림을 자동화하는 통합 피트니스 운영 플랫폼. Spring Boot 3.5 / Java 21 / PostgreSQL / Redis / RabbitMQ 기반.

---

## 빌드 및 실행 명령어

```bash
# 빌드
./gradlew build

# 로컬 실행 (Redis, RabbitMQ 없이 PostgreSQL만으로 실행)
./gradlew bootRun --args='--spring.profiles.active=local'

# 테스트 실행
./gradlew test

# 단일 테스트 클래스 실행
./gradlew test --tests "org.escape.gx.GxApplicationTests"

# 클린 빌드
./gradlew clean build
```

**로컬 환경 설정:** `application-local.yml`은 Redis·RabbitMQ 자동 설정을 제외하므로, 개발 시 PostgreSQL(localhost:5432, DB: `gx`)만 필요하다.

**Swagger UI:** 서버 실행 후 `http://localhost:8080/swagger-ui/index.html` 에서 API 확인 가능.

---

## 코드 아키텍처

### 패키지 구조

```
src/main/java/org/escape/gx/
├── api/                    # REST Controllers + DTO
│   ├── auth/               # 인증 (회원가입, 로그인, 토큰 갱신)
│   ├── gx/                 # GX 세션 조회·예약·취소
│   ├── membership/         # 회원권 조회·발급
│   └── demo/               # 테스트용 데모 UI
├── common/
│   ├── ErrorResponse.java
│   └── enums/              # 모든 Enum 정의
├── config/                 # Security, JWT, CORS, 전역 예외 처리
├── domain/                 # JPA Entity + Repository
│   ├── account/            # Account (계정), UserProfile (사용자 정보)
│   ├── gx/                 # GxClassInfo (기준정보), GxSession (세션), Reservation (예약)
│   ├── membership/         # Membership (회원권), MembershipLedger (원장)
│   └── payment/            # Payment (결제)
└── service/                # 비즈니스 로직
    ├── AuthService.java
    ├── ReservationService.java
    └── MembershipService.java
```

### 레이어 흐름

```
Controller (api/) → Service (service/) → Repository (domain/) → PostgreSQL
```

- **인증:** JWT 기반 Stateless. Access Token 1시간, Refresh Token 7일. `JwtAuthenticationFilter`가 SecurityFilterChain에 등록됨.
- **동시성 제어:** 회원권 차감(`Membership`)과 세션 예약 인원(`GxSession`)은 pessimistic lock(`findByIdForUpdate`)으로 중복 차감 방지.
- **원장(Ledger):** 회원권 모든 증감은 `MembershipLedger`에 반드시 기록. 거래 유형은 `TransactionType` Enum 사용.
- **공개 엔드포인트:** `/api/auth/**`, `/api/health`, Swagger UI, `/demo/**`는 인증 없이 접근 가능.

### Redis 사용

- Refresh Token 저장
- GX 대기 리스트 (예약 대기 발생 시 요청 정보 임시 저장)

---

## DB 스키마

### PostgreSQL 주요 테이블

| 테이블 | 설명 |
|-------|------|
| `계정관리` (Account) | 이메일, 패스워드, 회원상태, 정기결제여부, 계정코드 |
| `사용자관리` (UserProfile) | 사용자명, 전화번호 |
| `회원권` (Membership) | 잔여횟수, 시작일/종료일, 회원권상태 |
| `회원권 원장` (MembershipLedger) | 거래유형, 거래수량, 거래후 수량, 세션ID |
| `GX 클래스 기준정보` (GxClassInfo) | 세션명, 강사ID, 시작/종료일시, 최대/최소인원, 필요회원권횟수, 요일 |
| `GX 클래스 세션` (GxSession) | 기준정보ID, 예약인원, 세션상태 |
| `GX 클래스 예약` (Reservation) | 사용자ID, 세션ID, 회원권ID, 차감횟수, 예약상태 |
| `결제` (Payment) | 결제방법, 금액, 결제상태, 회원권갯수 |
| `사용자 패널티 관리` | 패널티카운트, 시작/종료일자 |
| `원장 정합성 검증` | 원장합계, 실제잔액, 차이, 검증상태 |

### Redis 자료구조

```
GX 대기 리스트: { 대기열ID, GX세션ID, 사용자ID, 생성일시 }
RefreshToken:  { 사용자ID, RefreshToken, createdAt, modifiedAt }
```

---

## Enum 정의

| Enum | 값 |
|------|----|
| `UserStatus` | ACTIVE, INACTIVE(90일 미접속), LOCK(비밀번호 5회 오류), WITHDRAWN(탈퇴), SUSPENDED(패널티) |
| `AccountCode` | USER, ADMIN, INSTRUCTOR |
| `ClassStatus` | BEFORE_RESV, ON_RESV, RESV_FULL, IN_CLASS, TERMINATED |
| `MembershipStatus` | ACTIVE, EXPIRED, SUSPENDED |
| `ReservationStatus` | RESERVED, COMPLETED, CANCELLED |
| `PaymentStatus` | COMPLETED, FAILED, REFUNDED |
| `SubscriptionActive` | Y, N |
| `TransactionType` | GRANT(+10 발급), DEDUCT(-1 예약), REFUND(+1 취소), BONUS(+N), PENALTY_DEDUCT(-1 노쇼), ADMIN_INCREASE, ADMIN_DECREASE, EXPIRE(0 만료소멸), ADJUST(±N 정합성수정) |

---

## 개발 표준

### 네이밍 규칙

- **변수:** lowerCamelCase. boolean은 `is` prefix 사용 (`isEmpty` O, `isNotEmpty` X).
- **상수:** `UPPER_SNAKE_CASE`. 공통 타입 접두사 사용 (예: `COLOR_RED`).
- **메서드:** 동사 시작, lowerCamelCase (예: `sendMessage`).
- **클래스:** UpperCamelCase.
- **패키지:** 소문자.

### Javadoc 주석

비즈니스 로직이 있는 메서드에는 필수 작성:

```java
/**
 * 기능 설명
 *
 * @param paramName 파라미터 설명
 * @return 반환값 설명
 * @exception ExceptionType 발생 조건
 */
```

클래스 주석에는 `@author`, `@since`, `@see`, 변경 이력 포함.

### 커밋 메시지

```
<type>: <subject>  ← 50자 이내, 명령문, 마침표 금지

<body>             ← 무엇을·왜 설명, 각 행 72자 이내

<footer>           ← 선택. BREAKING CHANGE 명시 시 사용
```

커밋 타입: `feat`, `fix`, `build`, `chore`, `ci`, `docs`, `style`, `refactor`, `test`, `perf`
