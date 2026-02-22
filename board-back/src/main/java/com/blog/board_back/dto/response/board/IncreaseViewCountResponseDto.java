package com.blog.board_back.dto.response.board;

import org.springframework.http.HttpStatus;         
import org.springframework.http.ResponseEntity;    

import com.blog.board_back.common.ResponseCode;    
import com.blog.board_back.common.ResponseMessage; 
import com.blog.board_back.dto.response.ResponseDto; 

public class IncreaseViewCountResponseDto extends ResponseDto {

  // 조회수 증가 성공 시 사용할 private 생성자
  private IncreaseViewCountResponseDto() {
    super(ResponseCode.SUCCESS, ResponseMessage.SUCCESS); // 성공 코드/메시지 세팅
  }

  // 조회수 증가 성공 응답 생성용 static 메서드
  public static ResponseEntity<IncreaseViewCountResponseDto> success() {
    IncreaseViewCountResponseDto result = new IncreaseViewCountResponseDto(); // DTO 생성
    return ResponseEntity.status(HttpStatus.OK).body(result); // 200 으로 반환
  }

  // 게시글이 존재하지 않을 때 실패 응답
  public static ResponseEntity<ResponseDto> noExistBoard() {
    ResponseDto result = new ResponseDto(ResponseCode.NOT_EXISTED_BOARD, ResponseMessage.NOT_EXISTED_BOARD);
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result); // 400 반환
  }
  
}
