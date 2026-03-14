import { getSignInUserRequest, refreshTokenRequest } from 'apis';
import { ResponseDto } from 'apis/response';
import { GetSignInUserResponseDto } from 'apis/response/user';
import { AUTH_PATH, BOARD_DETAIL_PATH, BOARD_PATH, BOARD_UPDATE_PATH, BOARD_WRITE_PATH, MAIN_PATH, SEARCH_PATH, USER_PATH } from 'constant';
import Container from 'layouts/Container';
import { useEffect } from 'react';
import { Route, Routes } from 'react-router-dom';
import { useLoginUserStore } from 'stores';
import { User } from 'types/interface';
import Authentication from 'views/Authentication';
import BoardDetail from 'views/Board/Detail';
import BoardUpdate from 'views/Board/Update';
import BoardWrite from 'views/Board/Write';
import Main from 'views/Main';
import Search from 'views/Search';
import UserP from 'views/User';
import './App.css';


//          component: Application 컴포넌트          //
function App() {

  //          state: 로그인 유저 전역 상태          //
  const { accessToken, setAccessToken, setLoginUser, resetLoginUser } = useLoginUserStore();

  //          function: get sign in response 처리 함수          //
  const getSignInUserResponse = (responseBody: GetSignInUserResponseDto | ResponseDto | null) => {
    if(!responseBody) return;
    const { code } = responseBody;
    if (code === 'AF' || code === 'NU') {
      resetLoginUser();
      return;
    }
    if (code === 'DBE') {
      resetLoginUser();
      return;
    }
    const loginUser: User = { ...responseBody as GetSignInUserResponseDto };
    setLoginUser(loginUser);
  }

  //          effect: 마운트 시 RefreshToken으로 세션 복원          //
  useEffect(() => {
    if (accessToken) return;
    // 새로고침 등으로 메모리가 비었을 때 HttpOnly 쿠키의 RefreshToken으로 복원 시도
    refreshTokenRequest().then(response => {
      if (!response || response.code !== 'SU') {
        resetLoginUser();
        return;
      }
      const { token } = response as { token: string; code: string };
      setAccessToken(token);
    });
  // eslint-disable-next-line react-hooks/exhaustive-deps
  }, []);

  //          effect: accessToken 변경 시 유저 정보 조회          //
  useEffect(() => {
    if (!accessToken) return;
    getSignInUserRequest().then(getSignInUserResponse);
  // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [accessToken]);

  //          render: Application 렌더링          //
  // description: 메인 화면 : '/' - Main //
  // description: 로그인 + 회원가입 화면 : '/auth' - Authentication //
  // description: 검색 화면 : '/search/:searchWord' - Search //
  // description: 유저 페이지 : '/user/:userEmail' - User //
  // description: 게시물 상세보기 : '/board/detail/:boardNumber' - BoardDetail //
  // description: 게시물 작성하기 : '/board/write' - BoardWrite //
  // description: 게시물 수정하기 : '/board/update/:boardNumber' - BoardUpdate //
  return (
    <Routes>
      <Route element={<Container />}>
        <Route path={MAIN_PATH()} element={<Main />} />
        <Route path={AUTH_PATH()} element={<Authentication />} />
        <Route path={SEARCH_PATH(':searchWord')} element={<Search />} />
        <Route path={USER_PATH(':userEmail')} element={<UserP />} />
        <Route path={BOARD_PATH()}>
          <Route path={BOARD_WRITE_PATH()} element={<BoardWrite />} />
          <Route path={BOARD_DETAIL_PATH(':boardNumber')} element={<BoardDetail />} />
          <Route path={BOARD_UPDATE_PATH(':boardNumber')} element={<BoardUpdate />} />
        </Route>
        <Route path='*' element={<h1>404 Not Found</h1>} />
      </Route>
    </Routes>
  );
}

export default App;
