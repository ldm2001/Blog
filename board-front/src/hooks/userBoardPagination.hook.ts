import { useEffect, useState } from 'react';
import { getUserBoardListRequest } from 'apis';
import { GetUserBoardListResponseDto } from 'apis/response/user';
import { BoardListItem } from 'types/interface';

//          hook: 유저 게시글 페이지네이션 훅          //
const useUserBoardPagination = (userEmail: string | undefined) => {

  //          state: 현재 페이지          //
  const [currentPage, setCurrentPage] = useState<number>(1);
  //          state: 게시글 리스트          //
  const [boardList, setBoardList] = useState<BoardListItem[]>([]);
  //          state: 게시글 총 수          //
  const [boardCount, setBoardCount] = useState<number>(0);
  //          state: 전체 페이지 수          //
  const [totalPages, setTotalPages] = useState<number>(1);

  //          effect: userEmail 또는 currentPage 변경 시 유저 게시글 조회          //
  useEffect(() => {
    if (!userEmail) return;
    getUserBoardListRequest(userEmail, currentPage).then(responseBody => {
      if (!responseBody || responseBody.code !== 'SU') return;
      const { userBoardList, totalPages } = responseBody as GetUserBoardListResponseDto;
      setBoardList(userBoardList);
      setTotalPages(totalPages);
      setBoardCount(userBoardList.length);
    });
  }, [userEmail, currentPage]);

  return { currentPage, setCurrentPage, boardList, boardCount, totalPages };

};

export default useUserBoardPagination;
