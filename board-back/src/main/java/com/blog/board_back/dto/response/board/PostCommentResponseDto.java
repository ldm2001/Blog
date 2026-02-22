package com.blog.board_back.dto.response.board;

import org.springframework.http.HttpStatus;         
import org.springframework.http.ResponseEntity;    

import com.blog.board_back.common.ResponseCode;    
import com.blog.board_back.common.ResponseMessage; 
import com.blog.board_back.dto.response.ResponseDto; 

import lombok.Getter; 

@Getter // 모든 필드의 getter 메서드 자동 생성
public class PostCommentResponseDto extends ResponseDto {

  // 댓글 등록 성공 시 사용할 private 생성자
  private PostCommentResponseDto() {
    super(ResponseCode.SUCCESS, ResponseMessage.SUCCESS); // 성공 코드/메시지 세팅
  }

  // 댓글 등록 성공(200 OK) 응답 생성용 static 메서드
  public static ResponseEntity<PostCommentResponseDto> success() {
    PostCommentResponseDto result = new PostCommentResponseDto(); // DTO 생성
    return ResponseEntity.status(HttpStatus.OK).body(result); // 200 으로 반환
  }

  // 게시글이 존재하지 않을 때 실패 응답
  public static ResponseEntity<ResponseDto> noExistBoard() {
    ResponseDto result = new ResponseDto(ResponseCode.NOT_EXISTED_BOARD, ResponseMessage.NOT_EXISTED_BOARD);
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result); // 400 반환
  }

  // 사용자가 존재하지 않을 때 실패 응답
  public static ResponseEntity<ResponseDto> noExistUser() {
    ResponseDto result = new ResponseDto(ResponseCode.NOT_EXISTED_USER, ResponseMessage.NOT_EXISTED_USER);
    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(result); // 401 반환
  }
  
}
