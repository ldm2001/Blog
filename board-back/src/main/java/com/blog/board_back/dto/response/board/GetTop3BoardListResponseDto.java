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
public class GetTop3BoardListResponseDto extends ResponseDto {

    // 좋아요 기준 Top 3 게시글 리스트
    private List<BoardListItem> top3List;

    // 게시글 엔티티 리스트를 받아서 DTO 리스트로 변환하는 private 생성자
    private GetTop3BoardListResponseDto(List<BoardListViewEntity> boardListViewEntities) {
        super(ResponseCode.SUCCESS, ResponseMessage.SUCCESS); // 성공 코드/메시지 세팅
        this.top3List = BoardListItem.getList(boardListViewEntities); // DTO 리스트 변환
    }

    // Top 3 게시글 조회 성공 응답 생성용 static 메서드
    public static ResponseEntity<GetTop3BoardListResponseDto> success(List<BoardListViewEntity> boardListViewEntities) {
        GetTop3BoardListResponseDto result = new GetTop3BoardListResponseDto(boardListViewEntities); // DTO 생성
        return  ResponseEntity.status(HttpStatus.OK).body(result); // 200 으로 반환
    }

}
