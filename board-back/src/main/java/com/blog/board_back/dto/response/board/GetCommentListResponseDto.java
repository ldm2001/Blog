package com.blog.board_back.dto.response.board;

import java.util.List;

import org.springframework.http.HttpStatus;         
import org.springframework.http.ResponseEntity;    

import com.blog.board_back.common.ResponseCode;    
import com.blog.board_back.common.ResponseMessage; 
import com.blog.board_back.dto.object.CommentListItem; 
import com.blog.board_back.dto.response.ResponseDto;    
import com.blog.board_back.repository.resultSet.GetCommentListResultSet; 
import lombok.Getter; // getter 메서드 자동 생성

@Getter // 모든 필드의 getter 메서드 자동 생성
public class GetCommentListResponseDto extends ResponseDto {

  // 댓글 리스트
  private List<CommentListItem> commentList;

  // 댓글 결과 집합 리스트를 받아 댓글 DTO 리스트로 변환하는 생성자
  private GetCommentListResponseDto(List<GetCommentListResultSet> resultSets) {
    super(ResponseCode.SUCCESS, ResponseMessage.SUCCESS); // 성공 코드/메시지 세팅
    this.commentList = CommentListItem.copyList(resultSets); // ResultSet 변환
  }

  // 댓글 목록 조회 성공 응답 생성용 static 메서드
  public static ResponseEntity<GetCommentListResponseDto> success(List<GetCommentListResultSet> resultSets) {
    GetCommentListResponseDto result = new GetCommentListResponseDto(resultSets); // DTO 생성
    return ResponseEntity.status(HttpStatus.OK).body(result); // 200 으로 반환
  }

  // 게시글이 존재하지 않을 때 실패 응답
  public static ResponseEntity<ResponseDto> noExistBoard() {
    ResponseDto result = new ResponseDto(ResponseCode.NOT_EXISTED_BOARD, ResponseMessage.NOT_EXISTED_BOARD);
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result); // 400 반환
  }
  
}

