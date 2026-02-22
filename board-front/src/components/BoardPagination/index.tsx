import { Dispatch, SetStateAction } from 'react';
import './style.css';

//          interface: 게시판 페이지네이션 컴포넌트 Properties          //
interface Props {
  currentPage: number;
  totalPages: number;
  setCurrentPage: Dispatch<SetStateAction<number>>;
}

//          component: 게시판 페이지네이션 컴포넌트          //
export default function BoardPagination({ currentPage, totalPages, setCurrentPage }: Props) {

  //          state: 현재 섹션 (10페이지 단위)          //
  const section = Math.ceil(currentPage / 10);
  const startPage = (section - 1) * 10 + 1;
  const endPage = Math.min(section * 10, totalPages);
  const pages: number[] = [];
  for (let i = startPage; i <= endPage; i++) pages.push(i);

  //          event handler: 이전 섹션 클릭 이벤트 처리          //
  const onPreviousClickHandler = () => {
    if (section <= 1) return;
    setCurrentPage((section - 1) * 10);
  };

  //          event handler: 다음 섹션 클릭 이벤트 처리          //
  const onNextClickHandler = () => {
    if (section * 10 >= totalPages) return;
    setCurrentPage(section * 10 + 1);
  };

  //          render: 게시판 페이지네이션 렌더링          //
  return (
    <div id='pagination-wrapper'>
      <div className='pagination-change-link-box'>
        <div className='icon-box-small'>
          <div className='icon expand-left-icon'></div>
        </div>
        <div className='pagination-change-link-text' onClick={onPreviousClickHandler}>{'이전'}</div>
      </div>
      <div className='pagination-divider'>{'|'}</div>
      {pages.map(page =>
        page === currentPage ?
          <div key={page} className='pagination-text-active'>{page}</div> :
          <div key={page} className='pagination-text' onClick={() => setCurrentPage(page)}>{page}</div>
      )}
      <div className='pagination-divider'>{'|'}</div>
      <div className='pagination-change-link-box'>
        <div className='pagination-change-link-text' onClick={onNextClickHandler}>{'다음'}</div>
        <div className='icon-box-small'>
          <div className='icon expand-right-icon'></div>
        </div>
      </div>
    </div>
  );
}
