package com.blog.board_back.dto.response;

import org.springframework.http.HttpStatus;         
import org.springframework.http.ResponseEntity;    

import com.blog.board_back.common.ResponseCode;    
import com.blog.board_back.common.ResponseMessage; 

import lombok.AllArgsConstructor; 
import lombok.Getter;             

@Getter 
@AllArgsConstructor 
public class ResponseDto {

    // 공통 응답 코드 (예: "SUCCESS", "ERROR" 등)
    private String code;
    // 공통 응답 메시지 (예: "성공", "실패" 등)
    private String message;

    // 데이터베이스 에러 발생 시(500 Internal Server Error) 응답용 static 메서드
    public static ResponseEntity<ResponseDto> databaseError() {
        ResponseDto responseBody = new ResponseDto(ResponseCode.DATABASE_ERROR, ResponseMessage.DATABASE_ERROR);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseBody); // 500 반환
    }

    // 유효성 검증 실패 시(400 Bad Request) 응답용 static 메서드
    public static ResponseEntity<ResponseDto> validationFailed() {
        ResponseDto responseBody = new ResponseDto(ResponseCode.VALIDATION_FAILED, ResponseMessage.VALIDATION_FAILED);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseBody); // 400 반환
    }

}
