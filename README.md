# Readit - 도서 검색 및 도서관 정보 공유 플랫폼

Readit은 도서 검색부터 실시간 도서관 소장 정보 확인, 그리고 사용자 리뷰 공유까지 한 곳에서 해결할 수 있는 웹 어플리케이션입니다.

## 🚀 주요 기능

### 1. 도서 검색 및 정보 조회

- **네이버 도서 검색 API**를 활용한 실시간 도서 검색.
- 도서 상세 정보(저자, 출판사, 줄거리 등) 제공.
- **Caffeine Cache**를 적용하여 반복되는 검색 요청에 대한 응답 속도 최적화.

### 2. 도서관 소장 정보 확인

- **도서관 정보나루 API**를 연동하여 특정 도서를 소장하고 있는 내 주변 도서관 검색.
- 지역별(시/도, 구/군) 필터링 기능을 통해 정확한 도서관 위치 및 연락처 확인.

### 3. 사용자 리뷰 및 큐레이션

- 도서별 별점 및 텍스트 리뷰 작성 기능.
- **맞춤형 추천**: 사용자의 최근 조회 기록을 바탕으로 좋아할 만한 도서 추천.
- **실시간 랭킹**: 현재 인기 있는 도서 목록 제공.

### 4. 사용자 인증

- **OAuth 2.0** 기반 소셜 로그인 지원 (Google, Kakao).
- Spring Security를 활용한 안전한 사용자 인증 및 권한 관리.

## 🛠 Tech Stack

### Backend

- **Java 25**
- **Spring Boot 4.0.2**
- **Spring Security** (OAuth2 Client)
- **Spring Data JPA**
- **Database**: PostgreSQL / MySQL (Runtime), H2 (Test)
- **Caching**: Caffeine Cache
- **Build Tool**: Maven

### Frontend

- **Thymeleaf** (Template Engine)
- **Tailwind CSS v4**
- **JavaScript** (ES6+)
- **TomSelect** (Custom Selection UI)
- **Font Awesome** (Icons)

## 📂 프로젝트 구조

- `src/main/java/.../config`: 프로젝트 설정 (Security, HTTP Client 등)
- `src/main/java/.../controller`: API 및 뷰 컨트롤러
- `src/main/java/.../service`: 외부 API 연동 및 비즈니스 로직
- `src/main/java/.../dto`: 데이터 전송 객체
- `src/main/resources/templates`: Thymeleaf 템플릿 파일
- `src/main/resources/static`: 정적 자원 (CSS, JS, Fonts)
