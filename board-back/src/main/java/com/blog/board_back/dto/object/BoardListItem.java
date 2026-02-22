package com.blog.board_back.dto.object;

import com.blog.board_back.entity.BoardListViewEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter // 모든 필드의 getter 메서드 자동 생성
@NoArgsConstructor // 기본 생성자 자동 생성
@AllArgsConstructor // 모든 필드를 파라미터로 받는 생성자 자동 생성
public class BoardListItem {
    // 게시글 고유 번호
    private int boardNumber;
    // 게시글 제목
    private String title;
    // 게시글 내용
    private String content;
    // 게시글 대표 이미지
    private String boardTitleImage;
    // 게시글 좋아요 수
    private int favoriteCount;
    // 게시글 댓글 수
    private int commentCount;
    // 게시글 조회수
    private int viewCount;
    // 작성 날짜 및 시간
    private String writeDatetime;
    // 작성자 닉네임
    private String writerNickname;
    // 작성자 프로필 이미지
    private String writerProfileImage;

    // BoardListViewEntity를 BoardListItem으로 변환하는 생성자
    public BoardListItem(BoardListViewEntity boardListViewEntity) {
        this.boardNumber = boardListViewEntity.getBoardNumber();
        this.title = boardListViewEntity.getTitle();
        this.content = boardListViewEntity.getContent();
        this.boardTitleImage = boardListViewEntity.getTitleImage();
        this.favoriteCount = boardListViewEntity.getFavoriteCount();
        this.commentCount = boardListViewEntity.getCommentCount();
        this.viewCount = boardListViewEntity.getViewCount();
        this.writeDatetime = boardListViewEntity.getWriteDatetime();
        this.writerNickname = boardListViewEntity.getWriterNickname();
        this.writerProfileImage = boardListViewEntity.getWriterProfileImage();
    }

    // BoardListViewEntity 리스트를 BoardListItem 리스트로 변환하는 static 메서드
    public static List<BoardListItem> getList(List<BoardListViewEntity> boardListViewEntities) {
        List<BoardListItem> list = new ArrayList<>();
        // 모든 엔티티 객체를 BoardListItem으로 변환 후 리스트에 추가
        for (BoardListViewEntity boardListViewEntity : boardListViewEntities) {
            BoardListItem boardListItem = new BoardListItem(boardListViewEntity);
            list.add(boardListItem);
        }
        return list; // 변환된 리스트 반환
    }
}
