package com.blog.board_back.dto.response.auth;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.blog.board_back.common.ResponseCode;
import com.blog.board_back.common.ResponseMessage;
import com.blog.board_back.dto.response.ResponseDto;

import lombok.Getter;

@Getter // 모든 필드의 getter 메서드 자동 생성
public class SignInResponseDto extends ResponseDto {

  // JWT AccessToken 문자열
  private String token;
  // AccessToken 만료 시간 (초, 15분 = 900)
  private int expirationTime;

  // private 생성자: 성공시 응답 생성에만 사용
  private SignInResponseDto(String token) {
    super(ResponseCode.SUCCESS, ResponseMessage.SUCCESS); // 부모 클래스에 성공 코드/메시지 전달
    this.token = token; // 발급된 JWT AccessToken 값
    this.expirationTime = 900; // AccessToken 만료 시간 - 15분
  }

  // 로그인 성공 시 응답 객체 생성
  public static ResponseEntity<SignInResponseDto> success(String token) {
    SignInResponseDto result = new SignInResponseDto(token); // 성공 응답 객체 생성
    return ResponseEntity.status(HttpStatus.OK).body(result); // 200으로 반환
  }

  // 로그인 실패 시 응답 객체 생성
  public static ResponseEntity<ResponseDto> signInFail() {
    ResponseDto result = new ResponseDto(ResponseCode.SIGN_IN_FAIL, ResponseMessage.SIGN_IN_FAIL); // 실패 응답 생성
    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(result); // 401로 반환
  }

}
