import { suggestTitleRequest } from 'apis';
import { MAIN_PATH } from 'constant';
import { ChangeEvent, useEffect, useRef, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { useBoardStore, useLoginUserStore } from 'stores';
import './style.css';

//          component: 게시물 작성 화면 컴포넌트          //
export default function BoardWrite() {

  //          state: 제목 부분 요소 참조 상태          //
  const titleRef = useRef<HTMLTextAreaElement | null>(null);
  //          state: 내용 부분 요소 참조 상태          //
  const contentRef = useRef<HTMLTextAreaElement | null>(null);
  //          state: 파일 입력 요소 참조 상태          //
  const imageInputRef = useRef<HTMLInputElement | null>(null);

  //          state: 게시물 상태          //
   const { title, setTitle } = useBoardStore();
   const { content, setContent } = useBoardStore();
   const { boardImageFileList, setBoardImageFileList } = useBoardStore();
   const { resetBoard } = useBoardStore();

  //          state: 로그인 유저 상태          //
  const { loginUser } = useLoginUserStore();
  //          state: 게시물 이미지 미리보기 URL 상태          //
  const [imageUrls, setImageUrls] = useState<string[]>([]);

  //          state: AI 제목 추천 상태          //
  const [aiTitles, setAiTitles] = useState<string[]>([]);
  const [aiLoading, setAiLoading] = useState<boolean>(false);
  const [aiError, setAiError] = useState<boolean>(false);

  //          function: 네비게이트 함수          //
  const navigate = useNavigate();

  //          event handler: 제목 변경 이벤트 처리          //
  const onTitleChangeHandler = (event: ChangeEvent<HTMLTextAreaElement>) => {
    const { value } = event.target;
    setTitle(value);

    if (!titleRef.current) return;
    titleRef.current.style.height = 'auto';
    titleRef.current.style.height = `${titleRef.current.scrollHeight}px`;
  }
  //          event handler: 내용 변경 이벤트 처리          //
  const onContentChangeHandler = (event: ChangeEvent<HTMLTextAreaElement>) => {
    const { value } = event.target;
    setContent(value);

    if (!contentRef.current) return;
    contentRef.current.style.height = 'auto';
    contentRef.current.style.height = `${contentRef.current.scrollHeight}px`;
  }
  //          event handler: 이미지 변경 이벤트 처리          //
  const onImageChangeHandler = (event: ChangeEvent<HTMLInputElement>) => {
    if (!event.target.files || !event.target.files.length) return;
    const file = event.target.files[0];

    const imageUrl = URL.createObjectURL(file);
    const newImageUrls = imageUrls.map(item => item);
    newImageUrls.push(imageUrl);
    setImageUrls(newImageUrls);

    const newBoardImageFileList = boardImageFileList.map(item => item);
    newBoardImageFileList.push(file);
    setBoardImageFileList(newBoardImageFileList);

    if (!imageInputRef.current) return;
    imageInputRef.current.value = '';
  }

   //          event handler: 이미지 업로드 버튼 클릭 이벤트 처리          //
   const onImageUploadButtonClickHandler = () => {
    if (!imageInputRef.current) return;
    imageInputRef.current.click();
   }
  //          event handler: 이미지 닫기 버튼 클릭 이벤트 처리          //
  const onImageCloseButtonClickHandler = (deleteindex: number) => {
    if (!imageInputRef.current) return;
    imageInputRef.current.value = '';

    const newImageUrls = imageUrls.filter((url, index) => index !== deleteindex);
    setImageUrls(newImageUrls);

    const newBoardImageFileList = boardImageFileList.filter((file, index) => index !== deleteindex);
    setBoardImageFileList(newBoardImageFileList);
  }

  //          event handler: AI 제목 추천 버튼 클릭 이벤트 처리          //
  const onAiSuggestClickHandler = async () => {
    if (!content.trim()) return;
    setAiLoading(true);
    setAiError(false);
    setAiTitles([]);

    const response = await suggestTitleRequest(content);
    setAiLoading(false);

    if (!response || !response.suggestedTitles || response.suggestedTitles.length === 0) {
      setAiError(true);
      return;
    }
    setAiTitles(response.suggestedTitles);
  }

  //          event handler: 추천 제목 선택 이벤트 처리          //
  const onAiTitleSelectHandler = (selectedTitle: string) => {
    setTitle(selectedTitle);
    setAiTitles([]);
    if (!titleRef.current) return;
    titleRef.current.style.height = 'auto';
    titleRef.current.style.height = `${titleRef.current.scrollHeight}px`;
  }

  //          effect: 마운트 실행 함수          //
  useEffect(() => {
    if (!loginUser) {
      navigate(MAIN_PATH());
      return;
    }
    resetBoard();
  // eslint-disable-next-line react-hooks/exhaustive-deps
  }, []);

  //          render: 게시물 작성 화면 컴포넌트 렌더링          //
  return (
    <div id='board-write-wrapper'>
      <div className='board-write-container'>
        <div className='board-write-box'>
          <div className='board-write-title-box'>
            <textarea ref={titleRef} className='board-write-title-textarea' rows={1} placeholder='제목을 작성해주세요.' value={title} onChange={onTitleChangeHandler} />
          </div>
          {aiTitles.length > 0 && (
            <div className='board-write-suggestions-list'>
              <div className='board-write-suggestions-title'>{'AI 추천 제목 (클릭하면 제목으로 적용됩니다)'}</div>
              <ul>
                {aiTitles.map((t, i) => (
                  <li key={i} onClick={() => onAiTitleSelectHandler(t)}>{t}</li>
                ))}
              </ul>
            </div>
          )}
          {aiError && (
            <div className='board-write-suggestion-error'>{'AI 제목 추천에 실패했습니다. 내용을 더 작성한 후 다시 시도해주세요.'}</div>
          )}
          <div className='divider'></div>
          <div className='board-write-content-box'>
            <textarea ref={contentRef} className='board-write-content-textarea' placeholder='내용을 작성해주세요.' value={content} onChange={onContentChangeHandler} />
            <div className='board-write-utils-box'>
              <div className='icon-button' onClick={onImageUploadButtonClickHandler}>
                <div className='icon image-box-light-icon'></div>
              </div>
              <button
                className='board-write-ai-suggest-button'
                onClick={onAiSuggestClickHandler}
                disabled={aiLoading || !content.trim()}
              >
                {aiLoading ? '추천 중' : 'AI 제목 추천'}
              </button>
            </div>
            <input ref={imageInputRef} type="file" accept='image/*' className='hidden' onChange={onImageChangeHandler} />
          </div>
          <div className='board-write-images-box'>
            {imageUrls.map((imageUrl, index) =>
            <div className='board-write-image-box' key={index}>
              <img className='board-write-image' src={imageUrl} alt='' />
              <div className='icon-button image-close' onClick={() => onImageCloseButtonClickHandler(index)}>
                <div className='icon close-icon'></div>
              </div>
            </div>
            )}
          </div>
        </div>
      </div>
    </div>
  )
}
