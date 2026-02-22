package com.blog.board_back.controller;

import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.blog.board_back.dto.request.auth.RefreshTokenRequestDto;
import com.blog.board_back.dto.request.auth.SignInRequestDto;
import com.blog.board_back.dto.request.auth.SignUpRequestDto;
import com.blog.board_back.dto.response.auth.RefreshTokenResponseDto;
import com.blog.board_back.dto.response.auth.SignInResponseDto;
import com.blog.board_back.dto.response.auth.SignUpResponseDto;
import com.blog.board_back.service.AuthService;

import jakarta.validation.Valid; 
import lombok.RequiredArgsConstructor; 

@RestController 
@RequestMapping("/api/v1/auth") 
@RequiredArgsConstructor 
public class AuthController {

  // 회원가입, 로그인 로직을 처리
  private final AuthService authService;
  
  // 회원가입 요청을 처리하는 엔드포인트
  @PostMapping("/sign-up")
  public ResponseEntity<? super SignUpResponseDto> signUp(
    @RequestBody @Valid SignUpRequestDto requestBody // HTTP 요청의 JSON을 자바 객체로 매핑하고 유효성 검증 수행
  ) {
    // 실제 회원가입 비즈니스 로직을 서비스에 위임
    ResponseEntity<? super SignUpResponseDto> response = authService.signUp(requestBody);
    // 성공 혹은 실패에 따라 HTTP 응답 반환
    return response; 
  }

  // 로그인 요청을 처리하는 엔드포인트
  @PostMapping("/sign-in")
  public ResponseEntity<? super SignInResponseDto> signIn(
    @RequestBody @Valid SignInRequestDto requestBody // HTTP 요청의 JSON을 자바 객체로 매핑하고 유효성 검증 수행
  ) {
    // 로그인 비즈니스 로직을 서비스에 위임
    ResponseEntity<? super SignInResponseDto> response = authService.signIn(requestBody);
    // 결과에 따라 HTTP 응답 반환
    return response;
  }

  // AccessToken 갱신 요청을 처리하는 엔드포인트
  @PostMapping("/refresh")
  public ResponseEntity<? super RefreshTokenResponseDto> refreshToken(
    @RequestBody @Valid RefreshTokenRequestDto requestBody // Refresh Token을 요청 바디에서 수신
  ) {
    // 토큰 갱신 비즈니스 로직을 서비스에 위임
    ResponseEntity<? super RefreshTokenResponseDto> response = authService.refreshToken(requestBody);
    // 성공 시 새 AccessToken + RefreshToken, 실패 시 401 반환
    return response;
  }

}

