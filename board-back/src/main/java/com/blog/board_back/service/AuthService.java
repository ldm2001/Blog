package com.blog.board_back.service;

import org.springframework.http.ResponseEntity;

import com.blog.board_back.dto.request.auth.RefreshTokenRequestDto;
import com.blog.board_back.dto.request.auth.SignInRequestDto;
import com.blog.board_back.dto.request.auth.SignUpRequestDto;
import com.blog.board_back.dto.response.auth.RefreshTokenResponseDto;
import com.blog.board_back.dto.response.auth.SignInResponseDto;
import com.blog.board_back.dto.response.auth.SignUpResponseDto;

// 회원가입, 로그인, 토큰 갱신 관련 서비스 인터페이스
public interface AuthService {
  ResponseEntity<? super SignUpResponseDto> signUp(SignUpRequestDto dto); // 회원가입 처리
  ResponseEntity<? super SignInResponseDto> signIn(SignInRequestDto dto); // 로그인 처리
  ResponseEntity<? super RefreshTokenResponseDto> refreshToken(RefreshTokenRequestDto dto); // AccessToken 갱신 처리
}
