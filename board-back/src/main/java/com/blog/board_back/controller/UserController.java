package com.blog.board_back.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.blog.board_back.dto.request.user.PatchNicknameRequestDto;
import com.blog.board_back.dto.request.user.PatchProfileImageRequestDto;
import com.blog.board_back.dto.response.user.GetSignInUserResponseDto;
import com.blog.board_back.dto.response.user.GetUserBoardListResponseDto;
import com.blog.board_back.dto.response.user.GetUserResponseDto;
import com.blog.board_back.dto.response.user.PatchNicknameResponseDto;
import com.blog.board_back.dto.response.user.PatchProfileImageResponseDto;
import com.blog.board_back.service.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor; // final 필드 생성자 자동 생성

@RestController // REST API
@RequestMapping("/api/v1/user") // 모든 유저 관련 엔드포인트에 /api/v1/user 접두사 적용
@RequiredArgsConstructor // 생성자 자동 주입
public class UserController {

  // 유저 관련 서비스 의존성 주입
  private final UserService userService;

  // 로그인 유저 정보 조회
  @GetMapping("")
  public ResponseEntity<? super GetSignInUserResponseDto> getSignInUser(
      @AuthenticationPrincipal String email // 인증된 사용자의 이메일을 파라미터로 전달
  ) {
      // 서비스에서 로그인 유저 정보 조회
      ResponseEntity<? super GetSignInUserResponseDto> response = userService.getSignInUser(email);
      // 서비스 응답 반환
      return response;
  }

  // 특정 유저 프로필 조회
  @GetMapping("/{userEmail}")
  public ResponseEntity<? super GetUserResponseDto> getUser(
      @PathVariable("userEmail") String userEmail
  ) {
      ResponseEntity<? super GetUserResponseDto> response = userService.getUser(userEmail);
      return response;
  }

  // 유저가 작성한 게시글 목록 조회
  @GetMapping("/{userEmail}/board-list")
  public ResponseEntity<? super GetUserBoardListResponseDto> getUserBoardList(
      @PathVariable("userEmail") String userEmail
  ) {
      ResponseEntity<? super GetUserBoardListResponseDto> response = userService.getUserBoardList(userEmail);
      return response;
  }

  // 닉네임 수정
  @PatchMapping("/nickname")
  public ResponseEntity<? super PatchNicknameResponseDto> patchNickname(
      @RequestBody @Valid PatchNicknameRequestDto requestBody,
      @AuthenticationPrincipal String email
  ) {
      ResponseEntity<? super PatchNicknameResponseDto> response = userService.patchNickname(requestBody, email);
      return response;
  }

  // 프로필 이미지 수정
  @PatchMapping("/profile-image")
  public ResponseEntity<? super PatchProfileImageResponseDto> patchProfileImage(
      @RequestBody PatchProfileImageRequestDto requestBody,
      @AuthenticationPrincipal String email
  ) {
      ResponseEntity<? super PatchProfileImageResponseDto> response = userService.patchProfileImage(requestBody, email);
      return response;
  }

}
