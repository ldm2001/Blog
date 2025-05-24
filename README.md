# Spring-React-MySQL 블로그 

## 기술 스택
- **백엔드**: Spring Boot, MVC, JPA, MySQL
- **프론트엔드**: React, TypeScript, Axios, Zustand 
- **기타**: JSON, RESTful API

## 주요 기능 요약
- 사용자 인증 (로그인/회원가입, 쿠키 기반 자동 로그인)
- 게시판 기능 (글 작성, 조회, 수정, 삭제)
- 댓글 작성 및 목록 확인
- 게시글 즐겨찾기(좋아요) 기능
- 게시글 조회수 증가 및 인기글 기능

## 프론트엔드 및 백엔드 코드 요약

### 프론트엔드

#### App.tsx
- 인증 쿠키를 기반으로 사용자 정보 요청 후 전역 상태 저장 (`Zustand` 활용)
- 라우터 경로 설정 및 메인 구조 담당

#### index.tsx
- 앱 진입점, `React.StrictMode` 및 `BrowserRouter` 적용으로 안정성 확보

#### src/apis/index.ts
- API 도메인/라우터 분리 및 에러 핸들링 공통화
- 반복되는 패턴은 추상화하면 유지보수성 향상 가능

#### get-board.response.dto.ts
- 게시글 상세 정보 DTO 정의
- Board 인터페이스와 응답 상태 코드 ResponseDto 상속 구조로 깔끔하게 구성됨

### 백엔드

#### controller
- RESTful 엔드포인트를 정의하고 클라이언트 요청을 받아 서비스 로직으로 전달함
- 예: 게시글 작성 POST /api/board, 게시글 조회 GET /api/board/{id} 등

#### service
- 핵심 비즈니스 로직 처리 계층 
- 트랜잭션, 유효성 검사, 권한 확인 등의 주요 로직이 위치

#### repository
- DB와의 상호작용을 담당하는 인터페이스 (Spring Data JPA 기반)
- 자동 쿼리 메서드 또는 JPQL, Native Query 사용 가능

#### entity
- 실제 DB 테이블과 매핑되는 클래스들 (예: User, Board, Comment 등)
- @Entity, @Id, @OneToMany 등의 JPA 어노테이션 사용

#### dto
- 요청/응답을 위한 데이터 전송 객체
- 계층 간의 데이터 전달을 명확하게 분리하고 validation을 적용함

#### config
- CORS 설정, 시큐리티 설정, 서버 전역 환경설정

#### exception
- 커스텀 예외 클래스, 전역 예외 핸들링 등을 정의하여 안정적인 예외 처리 지원

#### application.properties
- DB 정보, 서버 포트, JPA 옵션, 시큐리티 키 등 프로젝트 설정을 정의

#### 추후 유저 프로필 수정 부분 제작

