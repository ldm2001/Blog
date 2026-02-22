package com.blog.board_back.dto.response.board;

import org.springframework.http.HttpStatus;         
import org.springframework.http.ResponseEntity;    

import com.blog.board_back.common.ResponseCode;    
import com.blog.board_back.common.ResponseMessage; 
import com.blog.board_back.dto.response.ResponseDto; 

import lombok.Getter; 

@Getter // 모든 필드의 getter 메서드 자동 생성
public class DeleteBoardResponseDto extends ResponseDto {
  
  // 게시글 삭제 성공 시 사용할 private 생성자
  private DeleteBoardResponseDto() {
    super(ResponseCode.SUCCESS, ResponseMessage.SUCCESS); // 부모에 성공 코드/메시지 전달
  }

  // 삭제 성공 응답 반환
  public static ResponseEntity<DeleteBoardResponseDto> success() {
    DeleteBoardResponseDto result = new DeleteBoardResponseDto(); // 성공 객체 생성
    return ResponseEntity.status(HttpStatus.OK).body(result); // 200 으로 반환
  }

  // 게시글이 존재하지 않을 때 응답
  public static ResponseEntity<ResponseDto> noExistBoard() {
    ResponseDto result = new ResponseDto(ResponseCode.NOT_EXISTED_BOARD, ResponseMessage.NOT_EXISTED_BOARD);
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result); // 400 반환
  }

  // 사용자가 존재하지 않을때 응답
  public static ResponseEntity<ResponseDto> noExistUser() {
    ResponseDto result = new ResponseDto(ResponseCode.NOT_EXISTED_USER, ResponseMessage.NOT_EXISTED_USER);
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
  }

  // 권한이 없을 때 응답
  public static ResponseEntity<ResponseDto> noPermission() {
    ResponseDto result = new ResponseDto(ResponseCode.NO_PERMISSION, ResponseMessage.NO_PERMISSION);
    return ResponseEntity.status(HttpStatus.FORBIDDEN).body(result); // 403 반환
  }

}
