## render: Application 렌더링

- description: 메인 화면 : '/' - Main 
- description: 로그인 + 회원가입 화면 : '/auth' - Authentication 
- description: 검색 화면 : '/search/:searchWord' - Search 
- description: 유저 페이지 : '/user/:userEmail' - User
- description: 게시물 상세보기 : '/board/detail/:boardNumber' - BoardDetail 
- description: 게시물 작성하기 : '/board/write' - BoardWrite 
- description: 게시물 수정하기 : '/board/update/:boardNumber' - BoardUpdate  

## API 엔드포인트 주소

- http://localhost:4000/api/v1/auth/sign-up - Post
- http://localhost:4000/api/v1/auth/sign-in - Post
- http://localhost:4000/api/v1/user - Get
- http://localhost:4000/file/upload - Post
- http://localhost:4000/api/v1/board - Post
- http://localhost:4000/api/v1/board/3/favorite - Put
- http://localhost:4000/api/v1/board/5/favorite-list - Get
- http://localhost:4000/api/v1/board/2/comment - Post