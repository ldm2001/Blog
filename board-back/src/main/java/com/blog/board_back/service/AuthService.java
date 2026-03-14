package com.blog.board_back.service;

import org.springframework.http.ResponseEntity;

import com.blog.board_back.dto.request.auth.PasswordConfirmRequestDto;
import com.blog.board_back.dto.request.auth.SignInRequestDto;
import com.blog.board_back.dto.request.auth.SignUpRequestDto;
import com.blog.board_back.dto.response.ResponseDto;
import com.blog.board_back.dto.response.auth.RefreshTokenResponseDto;
import com.blog.board_back.dto.response.auth.SignInResponseDto;
import com.blog.board_back.dto.response.auth.SignUpResponseDto;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

// 회원가입, 로그인, 토큰 갱신 관련 서비스 인터페이스
public interface AuthService {
  ResponseEntity<? super SignUpResponseDto> signUp(SignUpRequestDto dto); // 회원가입 처리
  ResponseEntity<? super SignInResponseDto> signIn(SignInRequestDto dto, HttpServletResponse response); // 로그인 처리
  ResponseEntity<? super RefreshTokenResponseDto> refreshToken(HttpServletRequest request, HttpServletResponse response); // AccessToken 갱신 처리
  ResponseEntity<ResponseDto> signOut(HttpServletRequest request, HttpServletResponse response); // Refresh Token 폐기 처리
  ResponseEntity<ResponseDto> confirmPassword(PasswordConfirmRequestDto dto, String email); // 민감 작업 재인증
}
