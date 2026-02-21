# GX 백엔드 실행 방법

## 1. 준비

- **Java 21** 설치
- **PostgreSQL** 실행 중 (로컬 또는 Docker)
- **Gradle** 설치 (wrapper 생성용, 한 번만 필요)

## 2. DB 생성

```bash
# 로컬 PostgreSQL
createdb gx

# 또는 Docker
docker run -d --name gx-db -p 5432:5432 -e POSTGRES_PASSWORD=postgres -e POSTGRES_DB=gx postgres:16
```

## 3. Gradle Wrapper 생성 (최초 1회)

프로젝트 루트에서:

```bash
gradle wrapper
```

Gradle이 없다면:

- **Mac**: `brew install gradle` 후 위 명령 실행
- **Windows**: https://gradle.org/install/ 참고

## 4. 애플리케이션 실행

```bash
./gradlew bootRun --args='--spring.profiles.active=local'
```

- `local` 프로파일: Redis·RabbitMQ 없이 **PostgreSQL만** 사용
- 기본 DB: `localhost:5432`, DB명 `gx`, 사용자/비밀번호 `postgres`

## 5. 확인

- **데모 화면**: http://localhost:8080/demo/index.html  
  (회원가입 → 로그인 → "회원권 10회 발급" → 예약/취소)
- **API 문서**: http://localhost:8080/swagger-ui.html
- **헬스**: http://localhost:8080/api/health

## DB 설정 변경

환경 변수로 지정:

```bash
export DB_USERNAME=myuser
export DB_PASSWORD=mypass
./gradlew bootRun --args='--spring.profiles.active=local'
```

또는 `src/main/resources/application.yml`에서 `spring.datasource` 수정.
