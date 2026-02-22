package com.blog.board_back.dto.response.board;

import org.springframework.http.HttpStatus;         
import org.springframework.http.ResponseEntity;    

import com.blog.board_back.common.ResponseCode;    
import com.blog.board_back.common.ResponseMessage; 
import com.blog.board_back.dto.response.ResponseDto; 

import lombok.Getter; 

@Getter // 모든 필드의 getter 메서드 자동 생성
public class PostBoardResponseDto extends ResponseDto {

  // 게시글 등록 성공 시 사용할 private 생성자
  private PostBoardResponseDto() {
    super(ResponseCode.SUCCESS, ResponseMessage.SUCCESS); // 성공 코드/메시지 세팅
  }

  // 게시글 등록 성공 응답 생성용 static 메서드
  public static ResponseEntity<PostBoardResponseDto> success() {
    PostBoardResponseDto result = new PostBoardResponseDto(); // DTO 생성
    return ResponseEntity.status(HttpStatus.OK).body(result); // 200 반환
  }

  // 사용자가 존재하지 않을 때 실패 응답
  public static ResponseEntity<ResponseDto> noExistUser() {
    ResponseDto result = new ResponseDto(ResponseCode.NOT_EXISTED_USER, ResponseMessage.NOT_EXISTED_USER);
    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(result); // 401 반환
  }
  
}
