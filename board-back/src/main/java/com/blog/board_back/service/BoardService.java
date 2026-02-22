package com.blog.board_back.service;

import com.blog.board_back.dto.response.board.*;
import org.springframework.http.ResponseEntity;
import com.blog.board_back.dto.request.board.PatchBoardRequestDto;
import com.blog.board_back.dto.request.board.PostBoardRequestDto;
import com.blog.board_back.dto.request.board.PostCommentRequestDto;

// 게시판 관련 서비스 인터페이스
public interface BoardService {
  ResponseEntity<? super GetBoardResponseDto> getBoard(Integer boardNumber); // 게시글 상세 정보 조회
  ResponseEntity<? super GetFavoriteListResponseDto> getFavoriteList(Integer boardNumber); // 게시글에 좋아요 유저 리스트 조회
  ResponseEntity<? super GetCommentListResponseDto> getCommentList(Integer boardNumber); // 게시글의 댓글 리스트 조회
  ResponseEntity<? super GetLatestBoardListResponseDto> getLatestBoardList(); // 최신 게시글 전체 조회
  ResponseEntity<? super GetTop3BoardListResponseDto> getTop3BoardList(); // 인기 게시글 TOP3 리스트 조회
  ResponseEntity<? super GetSearchBoardListResponseDto> getSearchBoardList(String searchWord, String preSearchWord, int page); // 게시글 검색 결과 리스트 조회
  ResponseEntity<? super GetUserBoardListResponseDto> getUserBoardList(String email, int page); // 유저 게시글 리스트 조회
  ResponseEntity<? super PostBoardResponseDto> postBoard(PostBoardRequestDto dto, String email); // 게시글 등록
  ResponseEntity<? super PostCommentResponseDto> postComment(PostCommentRequestDto dto, Integer boardNumber, String email); // 게시글에 댓글 작성
  ResponseEntity<? super PutFavoriteResponseDto> putFavorite(Integer boardNumber, String email); // 게시글 좋아요 토글
  ResponseEntity<? super PatchBoardResponseDto> patchBoard(PatchBoardRequestDto dto, Integer boardNumber, String email); // 게시글 수정
  ResponseEntity<? super IncreaseViewCountResponseDto> increaseViewCount(Integer boardNumber); // 게시글 조회수 증가
  ResponseEntity<? super DeleteBoardResponseDto> deleteBoard(Integer boardNumber, String email); // 게시글 삭제

}
