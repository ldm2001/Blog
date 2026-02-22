package com.blog.board_back.dto.response.board;

import com.blog.board_back.common.ResponseCode;      
import com.blog.board_back.common.ResponseMessage;   
import com.blog.board_back.dto.object.BoardListItem; 
import com.blog.board_back.dto.response.ResponseDto; 

import com.blog.board_back.entity.BoardListViewEntity; 
import lombok.Getter; 
import org.springframework.http.HttpStatus;           
import org.springframework.http.ResponseEntity;        

import java.util.List;

@Getter // 모든 필드의 getter 메서드 자동 생성
public class GetLatestBoardListResponseDto extends ResponseDto {

    // 최신 게시글 리스트
    private List<BoardListItem> latestList;

    // 게시글 엔티티 리스트를 받아서 DTO 리스트로 변환하는 private 생성자
    private GetLatestBoardListResponseDto(List<BoardListViewEntity> boardEntities) {
        super(ResponseCode.SUCCESS, ResponseMessage.SUCCESS); // 성공 코드/메시지 세팅
        this.latestList = BoardListItem.getList(boardEntities); // DTO 리스트 변환
    }

    // 최신 게시글 목록 조회 성공(200 OK) 응답 생성용 static 메서드
    public static ResponseEntity<GetLatestBoardListResponseDto> success(List<BoardListViewEntity> boardEntities) {
        GetLatestBoardListResponseDto result = new GetLatestBoardListResponseDto(boardEntities); // DTO 생성
        return ResponseEntity.status(HttpStatus.OK).body(result); // 200 으로 반환
    }

}
