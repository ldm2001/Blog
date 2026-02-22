package com.blog.board_back.dto.response.board;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.blog.board_back.common.ResponseCode;
import com.blog.board_back.common.ResponseMessage;
import com.blog.board_back.dto.object.BoardListItem;
import com.blog.board_back.dto.response.ResponseDto;
import com.blog.board_back.entity.BoardListViewEntity;

import lombok.Getter;

@Getter // 모든 필드의 getter 메서드 자동 생성
public class GetSearchBoardListResponseDto extends ResponseDto {

    // 검색 결과 게시글 리스트
    private List<BoardListItem> searchList;
    // 전체 페이지 수
    private int totalPages;

    // 페이지 결과를 받아서 DTO로 변환하는 private 생성자
    private GetSearchBoardListResponseDto(Page<BoardListViewEntity> boardPage) {
        super(ResponseCode.SUCCESS, ResponseMessage.SUCCESS); // 성공 코드/메시지 세팅
        this.searchList = BoardListItem.getList(boardPage.getContent()); // DTO 리스트 변환
        this.totalPages = boardPage.getTotalPages();
    }

    // 게시글 검색 성공 응답 생성용 static 메서드
    public static ResponseEntity<GetSearchBoardListResponseDto> success(Page<BoardListViewEntity> boardPage) {
        GetSearchBoardListResponseDto result = new GetSearchBoardListResponseDto(boardPage); // DTO 생성
        return ResponseEntity.status(HttpStatus.OK).body(result); // 200 으로 반환
    }

}
