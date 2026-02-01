[프로젝트 설명]

# 📝 스마트 GX 예약 & 회원권 관리 시스템

## ✔️ 한 줄 소개

헬스장 GX(Group Exercise) 예약부터 회원권·패널티·보너스까지
**한 번에 관리하는 통합 피트니스 운영 플랫폼**

**GX 예약 · 회원권 차감 · 출결 · 패널티 · 보너스 · 알림**을 자동화하여
운영 효율과 사용자 경험을 동시에 개선합니다.

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

---

## ✔️ 기대 효과

* 운영 자동화를 통한 관리 비용 절감
* 예약·정산 오류 감소
* 데이터 신뢰도 향상
* 사용자 만족도 향상
* 관리자 업무 효율 증대
* 다지점 확장 기반 마련

---

## ✔️ 프로젝트 사용 도구

* 형상 관리: GitHub
* 커뮤니케이션: Slack
* 디자인: Figma
* CI: GitHub Actions
* CD: ArgoCD
* 모니터링: CloudWatch / Prometheus
* 문서화: Notion / Swagger (API 문서 v3)

## ✔️ 개발 환경

### Backend

| 이름              | 버전     |
| Java            | 21     |
| Spring Boot     | 3.5    |
| JPA / Hibernate | Latest |
| Spring Batch    | Latest |
| RabbitMQ        | Latest |

---

### Database

| 이름         | 버전            |
| PostgreSQL | 18.1          |
| Redis      | 8.4           | => Refresh Token, 예약 대기 발생 시 요청 정보 저장(일종의 global cache)

---

### DevOps

| 이름             | 버전     |
| GitHub Actions | -      |
| ArgoCD         | Latest |
| Slack Webhook  | -      |

---

### Infra

| 이름            | 설명           |
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


[DB 스키마 설계]
[PostgreSQL]
CREATE TABLE `사용자관리` (
`사용자ID`	VARCHAR(255)	NOT NULL,
`사용자명`	VARCHAR(255)	NULL,
`전화번호`	VARCHAR(255)	NULL
);

CREATE TABLE `회원권` (
`회원권ID`	VARCHAR	NOT NULL	DEFAULT NOT NULL,
`사용자ID`	VARCHAR(255)	NULL,
`결제ID`	VARCHAR	NULL,
`회원권상태`	VARCHAR(255)	NULL,
`시작일`	VARCHAR(255)	NULL,
`종료일`	VARCHAR(255)	NULL,
`발급일시`	VARCHAR(255)	NULL,
`잔여횟수`	VARCHAR(255)	NULL,
`초기발급횟수`	VARCHAR(255)	NULL
);

CREATE TABLE `원장 정합성 검증` (
`검증 ID`	VARCHAR(255)	NOT NULL,
`회원권 ID`	VARCHAR(255)	NULL,
`사용자 ID`	VARCHAR(255)	NULL,
`원장 합계`	VARCHAR(255)	NULL,
`실제 잔액`	VARCHAR(255)	NULL,
`차이`	VARCHAR(255)	NULL,
`검증 상태`	VARCHAR(255)	NULL,
`검증일시`	VARCHAR(255)	NULL
);

CREATE TABLE `GX 클래스 세션` (
`GX세션ID`	VARCHAR(255)	NOT NULL,
`GX 기준정보 ID`	VARCHAR(255)	NOT NULL,
`예약 인원`	VARCHAR(255)	NULL,
`세션상태`	VARCHAR(255)	NULL,
`생성일시`	VARCHAR(255)	NULL,
`수정일시`	VARCHAR(255)	NULL
);

CREATE TABLE `회원권 원장` (
`원장ID`	VARCHAR(255)	NOT NULL,
`회원권ID`	VARCHAR(255)	NULL,
`사용자ID`	VARCHAR(255)	NULL,
`거래유형`	VARCHAR(255)	NULL,
`거래수량`	VARCHAR(255)	NULL,
`거래후 수량`	VARCHAR(255)	NULL,
`거래설명`	VARCHAR(255)	NULL,
`생성자`	VARCHAR(255)	NULL,
`생성일시`	VARCHAR(255)	NULL,
`세션ID`	VARCHAR(255)	NULL
);

CREATE TABLE `GX 클래스 예약` (
`예약 ID`	VARCHAR(255)	NOT NULL,
`사용자ID`	VARCHAR(255)	NOT NULL,
`GX세션ID`	VARCHAR(255)	NOT NULL,
`회원권 ID`	VARCHAR(255)	NULL,
`차감횟수`	VARCHAR(255)	NULL,
`예약 상태`	VARCHAR(255)	NULL,
`생성일시`	VARCHAR(255)	NULL,
`수정일시`	VARCHAR(255)	NULL
);

CREATE TABLE `사용자 패널티 관리` (
`패널티 시작 일자`	VARCHAR(255)	NULL,
`사용자ID`	VARCHAR(255)	NOT NULL,
`패널티 카운트`	VARCHAR(255)	NULL,
`패널티 종료일자`	VARCHAR(255)	NULL
);

CREATE TABLE `결제` (
`결제 ID`	VARCHAR(255)	NOT NULL,
`사용자 ID`	VARCHAR(255)	NULL,
`결제방법`	VARCHAR(255)	NULL,
`결제 금액`	VARCHAR(255)	NULL,
`결제 상태`	VARCHAR(255)	NULL,
`결제일시`	VARCHAR(255)	NULL,
`생성일시`	VARCHAR(255)	NULL,
`수정일시`	VARCHAR(255)	NULL,
`회원권갯수`	VARCHAR(255)	NULL
);

CREATE TABLE `GX 클래스 기준정보` (
`GX 기준정보 ID`	VARCHAR(255)	NOT NULL,
`세션명`	VARCHAR(255)	NULL,
`강사 사용자 ID`	VARCHAR(255)	NULL,
`세션시작일시`	VARCHAR(255)	NULL,
`세션종료일시`	VARCHAR(255)	NULL,
`최대수용인원`	VARCHAR(255)	NULL,
`최소예약인원`	VARCHAR(255)	NULL,
`필요회원권횟수`	VARCHAR(255)	NULL,
`생성일시`	VARCHAR(255)	NULL,
`수정일시`	VARCHAR(255)	NULL,
`요일`	Integer	NULL
);

CREATE TABLE `계정관리` (
`사용자ID`	VARCHAR(255)	NOT NULL,
`계정코드`	VARCHAR(255)	NULL,
`이메일주소`	VARCHAR(255)	NOT NULL,
`패스워드`	VARCHAR(255)	NULL,
`패스워드 틀림 카운트`	VARCHAR(255)	NULL,
`회원상태`	VARCHAR(255)	NULL,
`정기결제여부`	VARCHAR(255)	NULL,
`최종로그인시간`	VARCHAR(255)	NULL,
`결제일시`	VARCHAR(255)	NULL,
`결제방법`	VARCHAR(255)	NULL
);

ALTER TABLE `사용자관리` ADD CONSTRAINT `PK_사용자관리` PRIMARY KEY (
`사용자ID`
);

ALTER TABLE `회원권` ADD CONSTRAINT `PK_회원권` PRIMARY KEY (
`회원권ID`
);

ALTER TABLE `원장 정합성 검증` ADD CONSTRAINT `PK_원장 정합성 검증` PRIMARY KEY (
`검증 ID`
);

ALTER TABLE `GX 클래스 세션` ADD CONSTRAINT `PK_GX 클래스 세션` PRIMARY KEY (
`GX세션ID`
);

ALTER TABLE `회원권 원장` ADD CONSTRAINT `PK_회원권 원장` PRIMARY KEY (
`원장ID`
);

ALTER TABLE `GX 대기 리스트` ADD CONSTRAINT `PK_GX 대기 리스트` PRIMARY KEY (
`대기열ID`
);

ALTER TABLE `GX 클래스 예약` ADD CONSTRAINT `PK_GX 클래스 예약` PRIMARY KEY (
`예약 ID`
);

ALTER TABLE `사용자 패널티 관리` ADD CONSTRAINT `PK_사용자 패널티 관리` PRIMARY KEY (
`패널티 시작 일자`,
`사용자ID`
);

ALTER TABLE `결제` ADD CONSTRAINT `PK_결제` PRIMARY KEY (
`결제 ID`
);

ALTER TABLE `GX 클래스 기준정보` ADD CONSTRAINT `PK_GX 클래스 기준정보` PRIMARY KEY (
`GX 기준정보 ID`
);

ALTER TABLE `계정관리` ADD CONSTRAINT `PK_계정관리` PRIMARY KEY (
`사용자ID`
);

ALTER TABLE `사용자관리` ADD CONSTRAINT `FK_계정관리_TO_사용자관리_1` FOREIGN KEY (
`사용자ID`
)
REFERENCES `계정관리` (
`사용자ID`
);

ALTER TABLE `사용자 패널티 관리` ADD CONSTRAINT `FK_계정관리_TO_사용자 패널티 관리_1` FOREIGN KEY (
`사용자ID`
)
REFERENCES `계정관리` (
`사용자ID`
);

[Redis]
`GX 대기 리스트` (
`대기열ID`	String
`GX세션ID`	String
`사용자ID`	String
`생성일시`	String
)

RefreshToken (
`사용자ID`	String
`RefreshToken`	String
`(createdAt)`	String
`최근 로그인일시(modifiedAt)`	String
)


[Enum]
USER_STATUS | 회원 상태 | ACTIVE/INACTIVE/LOCK/SUSPENDED/WITHDRAWN
1. ACTIVE: 활성화
2. INACTIVE: 90일이상 미접속 휴면계정
3. LOCK: 비밀번호 5회 연속 틀림
4. WITHDRAWN: 탈퇴
5. SUSPENDED: 패널티 적용

SUBSCRIPTION_ACTIVE | 정기결제 등록 여부 | Y, N

TRANSACTION_TYPE | 거래 유형 | DEDUCT/REFUND/GRANT/BONUS/PENALTY_DEDUCT
| 거래 유형 | 방향 | 발생 시점 | 설명 |
| --- | --- | --- | --- |
| **GRANT** | +10 | 회원권 발급 | 월초 정기 발급, 신규 가입 |
| **DEDUCT** | -1 | GX 예약 | 정상 예약 시 차감 |
| **REFUND** | +1 | 예약 취소 | 취소로 인한 복구 |
| **BONUS** | +N | 보너스 지급 | 이벤트, 추천인 보상 |
| **PENALTY_DEDUCT** | -1 | 노쇼 | 노쇼 추가 패널티 |
| **ADMIN_INCREASE** | +N | 관리자 작업 | CS 보상 등 |
| **ADMIN_DECREASE** | -N | 관리자 작업 | 오발급 회수 등 |
| **EXPIRE** | 0 | 유효기간 만료 | 남은 횟수 소멸 |
| **ADJUST** | ±N | 정합성 오류 수정 | 데이터 정정 |

RESERVATION_STATUS | 예약 상태 | RESERVED/COMPLETED/CANCELLED

CLASS_STATUS | 클래스 상태 | BEFORE_RESV/ON_RESV/RESV_FULL/IN_CLASS/TERMINATED
**BEFORE_RESV** : 클래스 생성은 완료되었으나 예약 시작 시간 이전
**ON_RESV** : 예약 시작 시간 ~ 정원 도달로 예약 마감 전 / 정원 초과하지 않는 경우, 클래스 시작 시간 전
**RESV_FULL** : 신청 정원에 도달하여 예약 마감 (대기열 등록은 가능)
**TERMINATED** : 출석 체크 완료 (예약 및 대기열 등록 불가능)

PAYMENT_STATUS | 결제 상태 | COMPLETED/ FAILED/ REFUNDED

ACCOUNT_CODE | 회원 유형 | USER, ADMIN, INSTRUCTOR
**USER** : 일반 유저
**ADMIN** : 시스템 관리자
**INSTRUCTOR** : 강사(헬스 트레이너, 필라테스 강사 등)


[개발 표준]
## 🍒Naming Rule

### 변수

1. 변수명은 소문자로 시작하며, 대소문자를 혼용하여 사용할 수 있다.
2. 넓은 변수(의미가 있는 변수)는 해당 변수의 의미를 잘 표현할 수 있는 영단어로 작성하며, 좁은 변수(for문, while 문 등에서 사용되는 의미 없는 변수)는 scratch variabl(i, j, k…)를 사용한다.
3. boolen 값으로 사용하는 값은 ‘is’를 prefix로 붙여 작성하고, 긍정적인 명칭을 사용한다.
   ex) isEmpty(O), isNotEmpty(X)
4. 변수는 미리 선언하지 않고, 최초 사용시에 선언한다.

### 상수

1. UPPER_SANKE_CASE로 작성한다.
2. 공통타입의 이름을 접두사로 작성한다.
   ex) COLOR_RED = 1; COLOR_YELLOW = 2;

### 기타

1. **함수명**은 동사로 시작하며, lowerCamelCase로 작성한다.
   ex) sendMessage
2. **클래스명**은 UpperCamelCase로 작성한다.
   ex) Character, ImmutalbeList…
3. **패키지명**은 소문자로 작성한다.
4. 탑레벨 클래스(Top level class)는 소스 파일에 1개만 존재해야 한다.
5. 용어사전을 우선하여 작명하며 사용빈도가 높은 용어는 용어사전에 추가한다.

## 🍒주석

Javadocs형태로 작성하며 아래와 같은 내용을 필수로 포함한다.

1. **클래스 주석**
    - 설명
    - 작성자 (@author)
    - 생성일 (@since)
    - 참조 (@see)
    - 변경 History
2. **메소드 주석**
   비즈니스 로직을 포함하지 않는 메소드에는 추가하지 않으며, 비즈니스 로직이 포함된 메소드에는 필수로 작성한다.
    - 기능 설명
    - 매개변수 (@param)
    - 반환값 (@return)
    - 예외 (@exception)

## 🍒Git branch 전략 및 커밋 메시지

### 커밋 메시지 7가지 규칙

1.  제목과 본문을 **한 줄(빈 행) 띄어 구분**한다.
2. 제목은 **50자** 이내로 제한한다.
3. 제목 **첫 글자는 대 문자**로 작성한다.
4. 제목 끝에 **마침표(.) 금지**
5. 제목은 **명령문**으로, **과거형 X**
6. 본문의 **각 행은 72자 내**로 작성한다. (줄 바꿈 사용)
7. 본문은 어떻게 보다 **무엇을, 왜에 대하여 설명**한다.

### 커밋 메시지 구조

```jsx
<type>: <subject>

<body>

<footer>
```
<Commit message type>
| 타입 이름 | 내용 |
| feat | 새로운 기능에 대한 커밋 |
| fix | 버그 수정에 대한 커밋 |
| build | 빌드 관련 파일 수정 / 모듈 설치 또는 삭제에 대한 커밋 |
| chore | 그 외 자잘한 수정에 대한 커밋 |
| ci | ci 관련 설정 수정에 대한 커밋 |
| docs | 문서 수정에 대한 커밋 |
| style | 코드 스타일 혹은 포맷 등에 관한 커밋 |
| refactor | 코드 리팩토링에 대한 커밋 |
| test | 테스트 코드 수정에 대한 커밋 |
| perf | 성능 개선에 대한 커밋 |

<Footer>
- 푸터는 선택사항이며, 이슈 추적번호나 브레이킹 체인지 등을 명시할 때 사용
- 브레이킹 체인지

  우리가 만든 라이브러리나 API를 누군가 사용하고 있을 때, **이전 버전과 호환되지 않는 변경**을 의미합니다.

    - **API 변경:** 기존에 쓰던 파라미터가 삭제되거나 이름이 바뀐 경우
    - **응답 규격 변경:** JSON 결과값에서 필수 필드가 사라진 경우
    - **환경 변화:** 자바 11에서 쓰던 코드가 자바 17로 올리면서 더 이상 작동하지 않는 경우

  ### 2. 왜 커밋 메시지 Footer(바닥글)에 적나요?

  커밋 메시지 규격(Conventional Commits)에서는 브레이킹 체인지가 있을 때 **반드시** 명시하도록 되어 있습니다. 그래야 이 코드를 내려받는 동료들이 "아, 이거 받으면 내 코드도 수정해야겠구나!"라고 미리 대비할 수 있거든요.


```
feat: 결제 API의 응답 구조를 변경함

기존의 'amount' 필드를 'totalPrice'와 'tax'로 세분화함.

BREAKING CHANGE: 기존 결제 완료 페이지에서 'amount' 필드를 참조하는 모든 코드는 수정이 필요함.
```