package com.blog.board_back.exception;

import org.springframework.http.ResponseEntity;                
import org.springframework.http.converter.HttpMessageNotReadableException; 
import org.springframework.web.bind.MethodArgumentNotValidException;       
import org.springframework.web.bind.annotation.ExceptionHandler;           
import org.springframework.web.bind.annotation.RestControllerAdvice;       

import com.blog.board_back.dto.response.ResponseDto;          

@RestControllerAdvice // 모든 컨트롤러에서 발생하는 예외를 처리하는 글로벌 예외 처리 클래스임을 명시
public class BadRequestExceptionHandler {
  
  // 유효성 검증 실패시 발생 및 잘못된 JSON 형식 등 파싱 실패시 발생
  @ExceptionHandler({MethodArgumentNotValidException.class, HttpMessageNotReadableException.class})
  public ResponseEntity<ResponseDto> validationExceptionHandler(Exception exception) {
    // 유효성 검증 실패 응답 반환
    return ResponseDto.validationFailed(); 
  }

}
