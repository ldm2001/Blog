# 틈새록 — 바쁜 틈에 쓰는 기록

개인 블로그 풀스택 프로젝트 
Spring Boot + React + MySQL 기반으로 구현한 블로그 서비스

---

## 기술 스택

### Frontend
- **React 18** + **TypeScript** (Vite)
- React Router DOM v6
- Axios
- react-cookie
- react-quill (리치 텍스트 에디터)
- daum-postcode (주소 검색)

### Backend
- **Spring Boot 3.5** (Java 17)
- Spring Security + **JWT 인증**
- Spring Data JPA + Hibernate
- **MySQL**

---

## 구현 기능

### 인증 (Authentication)
- 이메일 + 비밀번호 기반 **회원가입 / 로그인**
- **2단계 회원가입 폼** — 1단계(이메일·비밀번호) → 2단계(닉네임·주소·프로필 이미지)
- Daum Postcode API를 통한 주소 입력
- 프로필 이미지 업로드 (파일 서버 저장)
- **JWT AccessToken** (15분) + **RefreshToken** (7일) 발급
  - RefreshToken은 UUID opaque token, DB 저장
  - **Token Rotation**: 갱신 시마다 새 RefreshToken 발급 (이전 토큰 자동 무효화)
  - AccessToken 만료 시 자동 갱신 (`POST /auth/refresh`)
- Spring Security 기반 엔드포인트별 인가 처리

### 게시물 (Board)
- **게시물 작성** — react-quill 리치 텍스트 에디터, 이미지 업로드 지원
- **게시물 조회** — 본문 + 작성자 정보 + 조회수 자동 증가
- **게시물 수정** — 작성자 본인만 가능
- **게시물 삭제** — 작성자 본인만 가능
- **좋아요(즐겨찾기)** — 토글 방식, 좋아요한 유저 목록 확인
- **댓글** — 작성, 목록 조회 (최신순)
- **랜덤 게시글 3개** 메인 상단 노출 (`ORDER BY RAND() LIMIT 3`)
- **최신 게시물 목록** 페이지네이션 (클라이언트 사이드)

### 검색 (Search)
- 제목 · 내용 통합 키워드 검색 (서버사이드 페이지네이션)
- **인기 검색어 TOP 15** 실시간 표시
- **연관 검색어** 표시 (이전 검색어 기반)
- 검색 로그 자동 저장

### 유저 페이지 (User)
- 다른 유저 프로필 조회
- 유저가 작성한 게시물 목록 (서버사이드 페이지네이션)
- **내 정보 수정** — 닉네임, 프로필 이미지 변경 (본인만)

---

## 화면 구성

| 경로 | 화면 | 설명 |
|------|------|------|
| `/` | Main | 메인 화면 — 랜덤 게시글 3개 + 최신 게시물 목록 + 인기 검색어 |
| `/auth` | Authentication | 로그인 / 회원가입 |
| `/search/:searchWord` | Search | 검색 결과 + 연관 검색어 |
| `/user/:userEmail` | User | 유저 프로필 + 작성 게시물 목록 |
| `/board/detail/:boardNumber` | BoardDetail | 게시물 상세 — 본문, 좋아요, 댓글 |
| `/board/write` | BoardWrite | 게시물 작성 (로그인 필요) |
| `/board/update/:boardNumber` | BoardUpdate | 게시물 수정 (작성자만) |

---

## API 엔드포인트

### Auth
| Method | URL | 설명 |
|--------|-----|------|
| POST | `/api/v1/auth/sign-up` | 회원가입 |
| POST | `/api/v1/auth/sign-in` | 로그인 (AccessToken + RefreshToken 반환) |
| POST | `/api/v1/auth/refresh` | AccessToken 갱신 |

### User
| Method | URL | 설명 |
|--------|-----|------|
| GET | `/api/v1/user` | 로그인 유저 정보 조회 |
| GET | `/api/v1/user/:email` | 특정 유저 정보 조회 |
| PATCH | `/api/v1/user/nickname` | 닉네임 수정 |
| PATCH | `/api/v1/user/profile-image` | 프로필 이미지 수정 |
| GET | `/api/v1/user/:email/board-list` | 유저 게시물 목록 |

### Board
| Method | URL | 설명 |
|--------|-----|------|
| GET | `/api/v1/board/latest-list` | 최신 게시물 전체 목록 |
| GET | `/api/v1/board/top-3` | 랜덤 게시글 3개 |
| GET | `/api/v1/board/:boardNumber` | 게시물 상세 조회 |
| GET | `/api/v1/board/:boardNumber/increase-view-count` | 조회수 증가 |
| GET | `/api/v1/board/:boardNumber/favorite-list` | 좋아요 목록 |
| GET | `/api/v1/board/:boardNumber/comment-list` | 댓글 목록 |
| POST | `/api/v1/board` | 게시물 작성 |
| POST | `/api/v1/board/:boardNumber/comment` | 댓글 작성 |
| PATCH | `/api/v1/board/:boardNumber` | 게시물 수정 |
| PUT | `/api/v1/board/:boardNumber/favorite` | 좋아요 토글 |
| DELETE | `/api/v1/board/:boardNumber` | 게시물 삭제 |
| GET | `/api/v1/board/search-list/:word?page=` | 검색 결과 (페이지네이션) |
| GET | `/api/v1/board/user-board-list/:email?page=` | 유저 게시물 (페이지네이션) |

### Search
| Method | URL | 설명 |
|--------|-----|------|
| GET | `/api/v1/search/:searchWord` | 인기 검색어 + 연관 검색어 |

### File
| Method | URL | 설명 |
|--------|-----|------|
| POST | `/file/upload` | 이미지 파일 업로드 |
| GET | `/file/:fileName` | 이미지 파일 제공 |

---

## 로컬 실행

### Backend (Spring Boot)
```bash
cd board-back
./mvnw spring-boot:run
```

### Frontend (Vite)
```bash
cd board-front
npm install
npm start
```

### MySQL
```sql
CREATE DATABASE board;
USE board;
```

---

## 개발 환경

- Node.js 18+
- Java 17
- MySQL 8.0
