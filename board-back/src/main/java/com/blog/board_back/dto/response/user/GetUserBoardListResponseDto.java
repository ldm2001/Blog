package com.blog.board_back.dto.response.user;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.blog.board_back.common.ResponseCode;
import com.blog.board_back.common.ResponseMessage;
import com.blog.board_back.dto.object.BoardListItem;
import com.blog.board_back.dto.response.ResponseDto;
import com.blog.board_back.entity.BoardListViewEntity;

import lombok.Getter;

@Getter
public class GetUserBoardListResponseDto extends ResponseDto {

    // 유저가 작성한 게시글 리스트
    private List<BoardListItem> userBoardList;

    // 게시글 엔티티 리스트를 받아서 DTO 리스트로 변환하는 private 생성자
    private GetUserBoardListResponseDto(List<BoardListViewEntity> boardListViewEntities) {
        super(ResponseCode.SUCCESS, ResponseMessage.SUCCESS);
        this.userBoardList = BoardListItem.getList(boardListViewEntities);
    }

    // 유저 게시글 목록 조회 성공(200 OK) 응답 생성용 static 메서드
    public static ResponseEntity<GetUserBoardListResponseDto> success(List<BoardListViewEntity> boardListViewEntities) {
        GetUserBoardListResponseDto result = new GetUserBoardListResponseDto(boardListViewEntities);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

}
