package com.blog.board_back.dto.response.board;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;         
import org.springframework.http.ResponseEntity;   

import com.blog.board_back.common.ResponseCode;    
import com.blog.board_back.common.ResponseMessage; 
import com.blog.board_back.dto.response.ResponseDto; 
import com.blog.board_back.entity.ImageEntity;       
import com.blog.board_back.repository.resultSet.GetBoardResultSet; 

import lombok.Getter; 

@Getter // 모든 필드의 getter 메서드 자동 생성
public class GetBoardResponseDto extends ResponseDto {

  // 게시글 고유 번호
  private int boardNumber;
  // 게시글 제목
  private String title;
  // 게시글 내용
  private String content;
  // 게시글 이미지 리스트
  private List<String> boardImageList;
  // 게시글 작성 일시
  private String writeDateTime;
  // 작성자 이메일
  private String writerEmail;
  // 작성자 닉네임
  private String writerNickname;
  // 작성자 프로필 이미지
  private String writerProfileImage;

  // 게시글 상세 정보 및 이미지 리스트를 받아 DTO로 변환하는 private 생성자
  private GetBoardResponseDto(GetBoardResultSet resultSet, List<ImageEntity> imageEntities) {
    super(ResponseCode.SUCCESS, ResponseMessage.SUCCESS); // 성공 코드/메시지 세팅

    // 이미지 엔티티 리스트를 String 리스트로 변환
    List<String> boardImageList = new ArrayList<>();
    for(ImageEntity imageEntity: imageEntities) {
      String boardImage = imageEntity.getImage(); // 이미지 파일명 또는 URL 추출
      boardImageList.add(boardImage); // 리스트에 추가
    }

    // 게시글 상세정보 할당
    this.boardNumber = resultSet.getBoardNumber();
    this.title = resultSet.getTitle();
    this.content = resultSet.getContent();
    this.boardImageList = boardImageList;
    this.writeDateTime = resultSet.getWriteDatetime();
    this.writerEmail = resultSet.getWriterEmail();
    this.writerNickname = resultSet.getWriterNickname();
    this.writerProfileImage = resultSet.getWriterProfileImage();
  }

  // 성공 응답 생성용 static 메서드
  public static ResponseEntity<GetBoardResponseDto> success(GetBoardResultSet resultSet, List<ImageEntity> imageEntities) {
    GetBoardResponseDto result = new GetBoardResponseDto(resultSet, imageEntities); // DTO 생성
    return ResponseEntity.status(HttpStatus.OK).body(result); // 200 으로 반환
  }

  // 게시글이 존재하지 않을 때 실패 응답
  public static ResponseEntity<ResponseDto> noExistBoard() {
    ResponseDto result = new ResponseDto(ResponseCode.NOT_EXISTED_BOARD, ResponseMessage.NOT_EXISTED_BOARD);
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result); // 400 반환
  }
  
}
