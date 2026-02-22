package com.blog.board_back.controller;

import com.blog.board_back.dto.request.board.PatchBoardRequestDto;
import com.blog.board_back.dto.response.board.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import com.blog.board_back.dto.request.board.PostBoardRequestDto;
import com.blog.board_back.dto.request.board.PostCommentRequestDto;
import com.blog.board_back.service.BoardService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController // REST API
@RequestMapping("/api/v1/board") // 모든 엔드포인트가 /api/v1/board로 시작
@RequiredArgsConstructor // 생성자 자동 생성
public class BoardController {

  // 게시글/댓글 관련 비즈니스 로직 처리 서비스 DI
  private final BoardService boardService;

  // 특정 게시글 상세 조회
  @GetMapping("/{boardNumber}")
  public ResponseEntity<? super GetBoardResponseDto> getBoard(
      @PathVariable("boardNumber") Integer boardNumber // URL에서 게시글 번호 추출
  ) {
    // 서비스에서 상세 정보 조회 후 응답
    ResponseEntity<? super GetBoardResponseDto> response = boardService.getBoard(boardNumber);
    return response;
  }

  // 해당 게시글을 좋아요한 유저 리스트 조회
  @GetMapping("/{boardNumber}/favorite-list")
  public ResponseEntity<? super GetFavoriteListResponseDto> getFavoriteList(
      @PathVariable("boardNumber") Integer boardNumber) {
    ResponseEntity<? super GetFavoriteListResponseDto> response = boardService.getFavoriteList(boardNumber);
    return response;
  }

  // 해당 게시글의 댓글 목록 조회
  @GetMapping("/{boardNumber}/comment-list")
  public ResponseEntity<? super GetCommentListResponseDto> getCommentList(
      @PathVariable("boardNumber") Integer boardNumber) {
    ResponseEntity<? super GetCommentListResponseDto> response = boardService.getCommentList(boardNumber);
    return response;
  }

  // 게시글 조회수 +1 증가
  @GetMapping("/{boardNumber}/increase-view-count")
  public ResponseEntity<? super IncreaseViewCountResponseDto> increaseViewCount(
      @PathVariable("boardNumber") Integer boardNumber) {
    ResponseEntity<? super IncreaseViewCountResponseDto> response = boardService.increaseViewCount(boardNumber);
    return response;
  }

  // 최신 게시글 목록 조회(최신순)
  @GetMapping("/latest-list")
  public ResponseEntity<? super GetLatestBoardListResponseDto> getLatestBoardList() {
    ResponseEntity<? super GetLatestBoardListResponseDto> response = boardService.getLatestBoardList();
    return response;
  }

  // 좋아요수 기준 TOP3 게시글 목록 조회
  @GetMapping("/top-3")
  public ResponseEntity<? super GetTop3BoardListResponseDto> getTop3BoardList() {
    ResponseEntity<? super GetTop3BoardListResponseDto> response = boardService.getTop3BoardList();
    return response;
  }

  // 게시글 검색 결과 조회 (페이지네이션)
  @GetMapping(value = { "/search-list/{searchWord}", "/search-list/{searchWord}/{preSearchWord}" })
  public ResponseEntity<? super GetSearchBoardListResponseDto> getSearchBoardList(
      @PathVariable("searchWord") String searchWord, // 현재 검색어
      @PathVariable(value = "preSearchWord", required = false) String preSearchWord, // 이전 검색어
      @RequestParam(value = "page", defaultValue = "1") int page // 페이지 번호
  ) {
    ResponseEntity<? super GetSearchBoardListResponseDto> response = boardService.getSearchBoardList(searchWord,
        preSearchWord, page);
    return response;
  }

  // 특정 유저가 작성한 게시글 목록 조회 (페이지네이션)
  @GetMapping("/user-board-list/{email}")
  public ResponseEntity<? super GetUserBoardListResponseDto> getUserBoardList(
      @PathVariable("email") String email, // 유저 이메일
      @RequestParam(value = "page", defaultValue = "1") int page // 페이지 번호
  ) {
    ResponseEntity<? super GetUserBoardListResponseDto> response = boardService.getUserBoardList(email, page);
    return response;
  }

  // 게시글 등록(작성)
  @PostMapping("")
  public ResponseEntity<? super PostBoardResponseDto> postBoard(
      @RequestBody @Valid PostBoardRequestDto requestBody, // 작성 정보
      @AuthenticationPrincipal String email // 현재 로그인한 유저 이메일
  ) {
    ResponseEntity<? super PostBoardResponseDto> response = boardService.postBoard(requestBody, email);
    return response;
  }

  // 게시글에 댓글 등록
  @PostMapping("/{boardNumber}/comment")
  public ResponseEntity<? super PostCommentResponseDto> postComment(
      @RequestBody @Valid PostCommentRequestDto requestBody, // 댓글 내용
      @PathVariable("boardNumber") Integer boardNumber, // 댓글 달 게시글 번호
      @AuthenticationPrincipal String email // 작성자 이메일
  ) {
    ResponseEntity<? super PostCommentResponseDto> response = boardService.postComment(requestBody, boardNumber, email);
    return response;
  }

  // 게시글 즐겨찾기/좋아요 토글
  @PutMapping("/{boardNumber}/favorite")
  public ResponseEntity<? super PutFavoriteResponseDto> putFavorite(
      @PathVariable("boardNumber") Integer boardNumber,
      @AuthenticationPrincipal String email // 현재 로그인한 유저 이메일
  ) {
    ResponseEntity<? super PutFavoriteResponseDto> response = boardService.putFavorite(boardNumber, email);
    return response;
  }

  // 게시글 일부 정보 수정
  @PatchMapping("/{boardNumber}")
  public ResponseEntity<? super PatchBoardResponseDto> patchBoard(
      @RequestBody @Valid PatchBoardRequestDto requestBody, // 수정할 필드
      @PathVariable("boardNumber") Integer boardNumber,
      @AuthenticationPrincipal String email // 수정하는 유저 이메일
  ) {
    ResponseEntity<? super PatchBoardResponseDto> response = boardService.patchBoard(requestBody, boardNumber, email);
    return response;
  }

  // 게시글 삭제
  @DeleteMapping("/{boardNumber}")
  public ResponseEntity<? super DeleteBoardResponseDto> deleteBoard(
      @PathVariable("boardNumber") Integer boardNumber,
      @AuthenticationPrincipal String email // 삭제 요청자 이메일
  ) {
    ResponseEntity<? super DeleteBoardResponseDto> response = boardService.deleteBoard(boardNumber, email);
    return response;
  }

}
