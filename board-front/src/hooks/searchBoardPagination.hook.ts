import { useEffect, useState } from 'react';
import { getBoardSearchListRequest } from 'apis';
import { GetSearchBoardListResponseDto } from 'apis/response/board';
import { BoardListItem } from 'types/interface';

//          hook: 검색 게시글 페이지네이션 훅          //
const useSearchBoardPagination = (searchWord: string) => {

  //          state: 현재 활성 검색어          //
  const [word, setWord] = useState<string>('');
  //          state: 이전 검색어 (연관 검색어 로깅에 활용)          //
  const [prevWord, setPrevWord] = useState<string | undefined>(undefined);
  //          state: 현재 페이지          //
  const [currentPage, setCurrentPage] = useState<number>(1);
  //          state: 검색 결과 게시글 리스트          //
  const [boardList, setBoardList] = useState<BoardListItem[]>([]);
  //          state: 검색 결과 총 게시물 수          //
  const [searchCount, setSearchCount] = useState<number>(0);
  //          state: 전체 페이지 수          //
  const [totalPages, setTotalPages] = useState<number>(1);

  //          effect: searchWord 변경 시 word/prevWord 업데이트 및 페이지 초기화          //
  useEffect(() => {
    if (!searchWord) return;
    if (searchWord !== word) {
      setPrevWord(word || undefined);
      setWord(searchWord);
      setCurrentPage(1);
    }
  }, [searchWord]);

  //          effect: word 또는 currentPage 변경 시 검색 결과 조회          //
  useEffect(() => {
    if (!word) return;
    getBoardSearchListRequest(word, currentPage, prevWord).then(responseBody => {
      if (!responseBody || responseBody.code !== 'SU') return;
      const { searchList, totalPages } = responseBody as GetSearchBoardListResponseDto;
      setBoardList(searchList);
      setTotalPages(totalPages);
      setSearchCount(searchList.length);
    });
  }, [word, currentPage]);

  return { currentPage, setCurrentPage, boardList, searchCount, totalPages };

};

export default useSearchBoardPagination;
