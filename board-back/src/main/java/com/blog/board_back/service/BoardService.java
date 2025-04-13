package com.blog.board_back.service;

import com.blog.board_back.dto.response.board.*;
import org.springframework.http.ResponseEntity;

import com.blog.board_back.dto.request.board.PatchBoardRequestDto;
import com.blog.board_back.dto.request.board.PostBoardRequestDto;
import com.blog.board_back.dto.request.board.PostCommentRequestDto;

public interface BoardService {
  ResponseEntity<? super GetBoardResponseDto> getBoard(Integer boardNumber);
  ResponseEntity<? super GetFavoriteListResponseDto> getFavoriteList(Integer boardNumber);
  ResponseEntity<? super GetCommentListResponseDto> getCommentList(Integer boardNumber);
  ResponseEntity<? super GetLatestBoardListResponseDto> getLatestBoardList();
  ResponseEntity<? super GetTop3BoardListResponseDto> getTop3BoardList();
  ResponseEntity<? super PostBoardResponseDto> postBoard(PostBoardRequestDto dto, String email);
  ResponseEntity<? super PostCommentResponseDto> postComment(PostCommentRequestDto dto, Integer boardNumber, String email);
  ResponseEntity<? super PutFavoriteResponseDto> putFavorite(Integer boardNumber, String email);
  ResponseEntity<? super PatchBoardResponseDto> patchBoard(PatchBoardRequestDto dto, Integer boardNumber, String email);
  ResponseEntity<? super IncreaseViewCountResponseDto> increaseViewCount(Integer boardNumber);
  ResponseEntity<? super DeleteBoardResponseDto> deleteBoard(Integer boardNumber, String email);
}
