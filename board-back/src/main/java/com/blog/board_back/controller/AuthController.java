package com.blog.board_back.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.blog.board_back.dto.request.auth.PasswordConfirmRequestDto;
import com.blog.board_back.dto.request.auth.SignInRequestDto;
import com.blog.board_back.dto.request.auth.SignUpRequestDto;
import com.blog.board_back.dto.response.ResponseDto;
import com.blog.board_back.dto.response.auth.RefreshTokenResponseDto;
import com.blog.board_back.dto.response.auth.SignInResponseDto;
import com.blog.board_back.dto.response.auth.SignUpResponseDto;
import com.blog.board_back.service.AuthService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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
    @RequestBody @Valid SignInRequestDto requestBody, // HTTP 요청의 JSON을 자바 객체로 매핑하고 유효성 검증 수행
    HttpServletResponse response
  ) {
    // 로그인 비즈니스 로직을 서비스에 위임
    ResponseEntity<? super SignInResponseDto> result = authService.signIn(requestBody, response);
    // 결과에 따라 HTTP 응답 반환
    return result;
  }

  // AccessToken 갱신 요청을 처리하는 엔드포인트
  @PostMapping("/refresh")
  public ResponseEntity<? super RefreshTokenResponseDto> refreshToken(
    HttpServletRequest request,
    HttpServletResponse response
  ) {
    // HttpOnly 쿠키의 Refresh Token으로 AccessToken 갱신
    ResponseEntity<? super RefreshTokenResponseDto> result = authService.refreshToken(request, response);
    // 성공 시 새 AccessToken 반환 + RefreshToken 쿠키 갱신 실패 시 401 반환
    return result;
  }

  // 로그아웃 요청을 처리하는 엔드포인트
  @PostMapping("/sign-out")
  public ResponseEntity<ResponseDto> signOut(
    HttpServletRequest request,
    HttpServletResponse response
  ) {
    ResponseEntity<ResponseDto> result = authService.signOut(request, response);
    return result;
  }

  // 민감 작업 재인증(비밀번호 확인) 엔드포인트
  @PostMapping("/confirm-password")
  public ResponseEntity<ResponseDto> confirmPassword(
    @RequestBody @Valid PasswordConfirmRequestDto requestBody,
    @AuthenticationPrincipal String email
  ) {
    ResponseEntity<ResponseDto> result = authService.confirmPassword(requestBody, email);
    return result;
  }

}
