package com.blog.board_back.service;

import org.springframework.http.ResponseEntity;

import com.blog.board_back.dto.request.user.PatchNicknameRequestDto;
import com.blog.board_back.dto.request.user.PatchProfileImageRequestDto;
import com.blog.board_back.dto.response.user.GetSignInUserResponseDto;
import com.blog.board_back.dto.response.user.GetUserBoardListResponseDto;
import com.blog.board_back.dto.response.user.GetUserResponseDto;
import com.blog.board_back.dto.response.user.PatchNicknameResponseDto;
import com.blog.board_back.dto.response.user.PatchProfileImageResponseDto;

// 사용자 관련 서비스 인터페이스
public interface UserService {
  ResponseEntity<? super GetSignInUserResponseDto> getSignInUser(String email); // 로그인된 사용자 정보 조회
  ResponseEntity<? super GetUserResponseDto> getUser(String userEmail); // 특정 유저 프로필 조회
  ResponseEntity<? super GetUserBoardListResponseDto> getUserBoardList(String userEmail); // 유저가 작성한 게시글 목록 조회
  ResponseEntity<? super PatchNicknameResponseDto> patchNickname(PatchNicknameRequestDto requestBody, String email); // 닉네임 수정
  ResponseEntity<? super PatchProfileImageResponseDto> patchProfileImage(PatchProfileImageRequestDto requestBody, String email); // 프로필 이미지 수정
}
