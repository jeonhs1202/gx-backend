# 📝 스마트 GX 예약 & 회원권 관리 시스템

## ✔️ 한 줄 소개

헬스장 GX(Group Exercise) 예약부터 회원권·패널티·보너스까지
**한 번에 관리하는 통합 피트니스 운영 플랫폼**

**GX 예약 · 회원권 차감 · 출결 · 패널티 · 보너스 · 알림**을 자동화하여
운영 효율과 사용자 경험을 동시에 개선합니다.

---

## ✔️ 기획 배경

헬스장 및 피트니스 센터 운영 시,

* GX 예약 관리
* 회원권 잔여 횟수 관리
* 노쇼(No-show) 관리
* 패널티 및 정지 정책 적용
* 보너스 정책 운영
* 정기 결제 및 배치 처리

등을 각각 수작업 또는 분산된 시스템으로 관리하는 경우가 많습니다.

이로 인해 다음과 같은 문제가 발생했습니다.

* 예약/취소 시 회원권 차감 오류
* 중복 차감 및 데이터 불일치
* 노쇼 및 패널티 관리의 비효율
* 정기 결제·발급 자동화 부족
* 운영 인력의 업무 부담 증가

⇒ 이러한 문제를 해결하기 위해
**예약·회원권·정산·정책·알림을 통합한 관리 시스템**을 구축하고자 본 프로젝트를 기획하였습니다.

---

## ✔️ 타겟

* GX 프로그램을 운영하는 헬스장 및 피트니스 센터
* 다수 회원을 관리하는 스포츠 시설 운영자
* 자동화된 예약·정산 시스템이 필요한 관리자
* 모바일 기반 예약 서비스를 이용하는 일반 회원

---

## ✔️ 주요 기능

### 1. 인증 · 계정 관리

* 이메일 기반 회원가입 / 로그인
* JWT 기반 인증
* 비밀번호 변경 / 찾기
* 내 정보 조회 및 수정
* 회원 탈퇴 및 자동 예약 정리

---

### 2. GX 예약 관리

* GX 클래스 예약
* 예약 취소
* 예약 내역 조회
* 세션 취소 시 일괄 환불 처리
* 예약 상태별 필터링 조회

---

### 3. 회원권 관리 시스템

#### 📌 회원권 발급

* 신규 결제 시 자동 발급
* 정기 결제 배치 발급
* 관리자 수동 발급

#### 📌 회원권 조회

* 잔여 횟수 조회
* 유효 기간 조회
* 상태 조회 (ACTIVE / EXPIRED / SUSPENDED)

#### 📌 회원권 차감·원복

* 예약 시 자동 차감
* 취소 시 자동 복구
* 세션 취소 시 일괄 복구
* 회원권 양도 처리
* 동시성 제어 기반 중복 차감 방지

---

### 4. 원장(Ledger) 관리

* 모든 증감 내역 자동 기록
* 거래 유형 관리 (DEDUCT / REFUND / BONUS 등)
* 거래 후 잔액 기록
* 회원별 거래 이력 조회
* 배치 기반 정합성 검증

---

### 5. 패널티 & 회원 상태 관리

* 노쇼 자동 기록
* 패널티 포인트 누적
* 정지 정책 자동 적용
* 정지 자동 해제
* 관리자 수동 제어
* 패널티 이력 조회

---

### 6. 보너스 정책 시스템

* 출석 기반 보너스 지급
* 월별 달성 보너스
* 중복 지급 방지
* 보너스 원장 기록
* 실시간 지급 지원

---

### 7. 알림 시스템

* 예약 완료 / 취소 알림
* GX 시작 리마인드
* 회원권 만료 안내
* 패널티 알림
* 보너스 지급 알림
* SMS / Email 연동

---

### 8. 관리자 기능

* 클래스 등록 및 관리
* 회원 상태 관리
* 회원권 관리
* 출결 관리
* 배치 작업 관리
* 통계 및 모니터링

---

## ✔️ 시스템 특징

### ✅ 안정성

* 트랜잭션 기반 처리
* 회원권 중복 차감 방지
* 원장 정합성 검증 배치

### ✅ 확장성

* 메시지 큐 기반 비동기 처리
* 배치 기반 대용량 처리
* 마이크로서비스 확장 가능 구조

### ✅ 자동화

* 정기 결제 자동 발급
* 만료 처리 자동화
* 보너스·패널티 자동 반영

### ✅ 보안성

* JWT 인증
* 개인정보 암호화
* HTTPS 통신
* 입력값 검증



## ✔️ 개발 환경

### Backend

| 이름              | 버전     |
| --------------- | ------ |
| Java            | 21     |
| Spring Boot     | 3.5    |
| JPA / Hibernate | Latest |
| Spring Batch    | Latest |
| RabbitMQ        | Latest |

---

### Database

| 이름         | 버전            |
| ---------- | ------------- |
| PostgreSQL | Latest Stable |
| Redis      | 8.4           |

---

### DevOps

| 이름             | 버전     |
| -------------- | ------ |
| Docker         | Latest |
| GitHub Actions | -      |
| ArgoCD         | Latest |
| Slack Webhook  | -      |

---

### Infra

| 이름            | 설명           |
| ------------- | ------------ |
| AWS ECR       | 컨테이너 이미지 저장소 |
| AWS Fargate   | 컨테이너 실행 환경   |
| Load Balancer | 트래픽 분산       |

---

## ✔️ 아키텍처 개요

```
[ Client ]
     ↓
[ API Gateway / ALB ]
     ↓
[ Spring Boot Service ]
     ↓
[ PostgreSQL / Redis / RabbitMQ ]
     ↓
[ Batch / Scheduler ]
```

* 실시간 처리 + 배치 처리 혼합 구조
* 메시지 기반 비동기 확장 가능

---

## ✔️ 향후 확장 계획

* 모바일 앱 연동
* AI 기반 출결/패턴 분석
* CRM 연동
* 다지점 통합 관리
* 관리자 대시보드 고도화
* 결제 PG 연동 확대

---

## ✔️ 빠른 시작 (로컬 실행)

### 요구 사항

* Java 21
* PostgreSQL (로컬 또는 Docker)

### 1. Java 21 설치

Java 21이 필요합니다. 설치되어 있지 않다면:

```bash
# Homebrew 사용 (macOS)
brew install openjdk@21
sudo ln -sfn $(brew --prefix openjdk@21)/libexec/openjdk.jdk /Library/Java/JavaVirtualMachines/openjdk-21.jdk

# 설치 확인
java -version  # "21.x.x" 출력 확인
```

### 2. PostgreSQL 설정

애플리케이션은 기본적으로 `postgres` 유저로 DB에 접속합니다.

#### 방법 A: postgres role 생성 (권장)

macOS에서 Homebrew로 PostgreSQL을 설치한 경우, 기본 유저가 `postgres`가 아닌 **현재 맥 계정명**으로 생성됩니다.
이 경우 아래와 같이 `postgres` role과 DB를 직접 생성해야 합니다.

```bash
# psql 접속 (맥 계정명으로 접속)
psql postgres

# psql 내에서 실행
CREATE ROLE postgres WITH LOGIN SUPERUSER PASSWORD 'postgres';
CREATE DATABASE gx OWNER postgres;
\q
```

#### 방법 B: Docker로 실행

```bash
docker run -d \
  --name gx-postgres \
  -p 5432:5432 \
  -e POSTGRES_USER=postgres \
  -e POSTGRES_PASSWORD=postgres \
  -e POSTGRES_DB=gx \
  postgres:16
```

#### 방법 C: 환경 변수로 기존 유저 지정

이미 다른 유저명으로 PostgreSQL이 설정된 경우:

```bash
# 현재 존재하는 유저 확인
psql postgres -c '\du'

# DB 생성
createdb gx

# 해당 유저명으로 실행
DB_USERNAME=<유저명> DB_PASSWORD=<비밀번호> ./gradlew bootRun --args='--spring.profiles.active=local'
```

### 3. 애플리케이션 실행

Redis / RabbitMQ 없이 PostgreSQL만으로 실행 (로컬 프로파일):

```bash
./gradlew bootRun --args='--spring.profiles.active=local'
```

기본 접속 설정: `localhost:5432`, DB명 `gx`, 사용자/비밀번호 `postgres`
환경 변수 `DB_USERNAME`, `DB_PASSWORD`로 변경 가능.

### 3. 데모 화면

브라우저에서 **http://localhost:8080/demo/index.html** 접속.

* **회원가입** → **로그인** → **회원권 10회 발급(데모)** 클릭 → **예약 가능 세션**에서 예약 → **내 예약**에서 취소 가능.

### 4. API 문서

* Swagger UI: **http://localhost:8080/swagger-ui.html**
* OpenAPI JSON: **http://localhost:8080/v3/api-docs**

---
