import { getRelationListRequest } from 'apis';
import { GetRelationListResponseDto } from 'apis/response/search';
import { ResponseDto } from 'apis/response';
import BoardItem from 'components/BoardItem';
import BoardPagination from 'components/BoardPagination';
import { SEARCH_PATH } from 'constant';
import { useSearchBoardPagination } from 'hooks';
import { useEffect, useState } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import './style.css';

//          component: 검색 화면 컴포넌트          //
export default function Search() {

  //          state: 검색어 path variable 상태          //
  const { searchWord } = useParams();

  //          function: 네비게이트 함수          //
  const navigate = useNavigate();

  //          state: 검색 페이지네이션 훅          //
  const { currentPage, setCurrentPage, boardList, searchCount, totalPages } =
    useSearchBoardPagination(searchWord || '');

  //          state: 연관 검색어 리스트 상태          //
  const [relativeWordList, setRelativeWordList] = useState<string[]>([]);

  //          function: get relation list response 처리 함수          //
  const getRelationListResponse = (responseBody: GetRelationListResponseDto | ResponseDto | null) => {
    if (!responseBody) return;
    const { code } = responseBody;
    if (code !== 'SU') return;

    const { relativeWordList } = responseBody as GetRelationListResponseDto;
    setRelativeWordList(relativeWordList);
  };

  //          event handler: 검색어 클릭 이벤트 처리          //
  const onWordClickHandler = (word: string) => {
    navigate(SEARCH_PATH(word));
  };

  //          effect: 검색어 변경 시 연관 검색어 조회          //
  useEffect(() => {
    if (!searchWord) return;
    getRelationListRequest(searchWord).then(getRelationListResponse);
  }, [searchWord]);

  //          render: 검색 화면 컴포넌트 렌더링          //
  return (
    <div id='search-wrapper'>
      <div className='search-container'>
        <div className='search-title-box'>
          <div className='search-title'>{`'${searchWord}' 검색 결과`}</div>
          <div className='search-count-box'>
            <div className='search-count'>
              <span className='search-count-emphasis'>{searchCount}</span>
              {'건'}
            </div>
          </div>
        </div>
        <div className='search-contents-box'>
          <div className='search-contents'>
            {searchCount === 0 ?
              <div className='search-contents-nothing'>{'검색 결과가 없습니다.'}</div> :
              boardList.map((boardListItem, index) => <BoardItem key={index} boardListItem={boardListItem} />)
            }
          </div>
          <div className='search-relation-word-box'>
            <div className='search-relation-word-card-box'>
              <div className='search-relation-word-card-title'>{'연관 검색어'}</div>
              <div className='search-relation-word-card-contents'>
                {relativeWordList.length === 0 ?
                  <div className='search-contents-nothing'>{'연관 검색어가 없습니다.'}</div> :
                  relativeWordList.map((word, index) =>
                    <div key={index} className='word-badge' onClick={() => onWordClickHandler(word)}>{word}</div>
                  )
                }
              </div>
            </div>
          </div>
        </div>
        <div className='search-pagination-box'>
          <BoardPagination
            currentPage={currentPage}
            totalPages={totalPages}
            setCurrentPage={setCurrentPage}
          />
        </div>
      </div>
    </div>
  );
}
