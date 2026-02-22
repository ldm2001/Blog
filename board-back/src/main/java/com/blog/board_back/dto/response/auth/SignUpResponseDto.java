package com.blog.board_back.dto.response.auth;

import org.springframework.http.HttpStatus;       
import org.springframework.http.ResponseEntity;   

import com.blog.board_back.common.ResponseCode;   
import com.blog.board_back.common.ResponseMessage;
import com.blog.board_back.dto.response.ResponseDto; 

import lombok.Getter; 

@Getter // 모든 필드의 getter 메서드 자동 생성
public class SignUpResponseDto extends ResponseDto {
  
    // 회원가입 성공 시 사용할 private 생성자
    private SignUpResponseDto() {
      super(ResponseCode.SUCCESS, ResponseMessage.SUCCESS); // 부모에 성공 코드/메시지 전달
    }

    // 회원가입 성공 응답 반환
    public static ResponseEntity<SignUpResponseDto> success() {
        SignUpResponseDto result = new SignUpResponseDto(); // 성공 응답 객체 생성
        return ResponseEntity.status(HttpStatus.OK).body(result); // 200 으로 반환
    }

    // 이메일 중복시 실패 응답
    public static ResponseEntity<ResponseDto> duplicateEmail() {
        ResponseDto result = new ResponseDto(ResponseCode.DUPLICATE_EMAIL, ResponseMessage.DUPLICATE_EMAIL); // 실패 코드/메시지
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result); // 400 으로 반환
    }

    // 닉네임 중복시 실패 응답
    public static ResponseEntity<ResponseDto> duplicateNickname() {
        ResponseDto result = new ResponseDto(ResponseCode.DUPLICATE_NICKNAME, ResponseMessage.DUPLICATE_NICKNAME);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
    }

    // 전화번호 중복시 실패 응답
    public static ResponseEntity<ResponseDto> duplicateTelNumber() {
        ResponseDto result = new ResponseDto(ResponseCode.DUPLICATE_TEL_NUMBER, ResponseMessage.DUPLICATE_TEL_NUMBER);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
    }

}