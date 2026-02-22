package com.blog.board_back.entity;

import java.text.SimpleDateFormat;        
import java.time.Instant;                 
import java.util.Date;                    

import com.blog.board_back.dto.request.board.PatchBoardRequestDto; 
import com.blog.board_back.dto.request.board.PostBoardRequestDto;  

import jakarta.persistence.Entity;         
import jakarta.persistence.GeneratedValue; 
import jakarta.persistence.GenerationType; 
import jakarta.persistence.Id;             
import jakarta.persistence.Table;          
import lombok.AllArgsConstructor;          
import lombok.Getter;                     
import lombok.NoArgsConstructor;           

@Getter // 모든 필드의 getter 메서드 자동 생성
@NoArgsConstructor // 파라미터 없는 기본 생성자 자동 생성
@AllArgsConstructor // 모든 필드를 파라미터로 받는 생성자 자동 생성
@Entity(name="board") // JPA 엔티티 이름 지정
@Table(name="board")  // DB 테이블명 지정
public class BoardEntity {

    // 게시글 고유 번호
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int boardNumber;
    // 게시글 제목
    private String title;
    // 게시글 본문 내용
    private String content;
    // 게시글 작성 일시
    private String writeDatetime;
    // 좋아요 수
    private int favoriteCount;
    // 댓글 수
    private int commentCount;
    // 조회수
    private int viewCount;
    // 작성자 이메일
    private String writerEmail;

    // 게시글 등록 시 DTO와 이메일을 받아 엔티티를 초기화하는 생성자
    public BoardEntity(PostBoardRequestDto dto, String email) {
        
        // 현재 시간을 yyyy-MM-dd HH:mm:ss 형식의 문자열로 변환
        Date now = Date.from(Instant.now());
        SimpleDateFormat simpleDateFormat =  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String writeDatetime = simpleDateFormat.format(now);

        this.title = dto.getTitle();
        this.content = dto.getContent();
        this.writeDatetime = writeDatetime;
        this.favoriteCount = 0;
        this.commentCount = 0;
        this.viewCount = 0;
        this.writerEmail = email;
    }

    // 게시글 조회수 증가
    public void increaseViewCount(){
        this.viewCount++;
    }    

    // 좋아요 수 증가
    public void increaseFavoriteCount() {
        this.favoriteCount++;
    }

    // 댓글 수 증가
    public void increaseCommentCount() {
        this.commentCount++;
    }

    // 좋아요 수 감소 (음수 방지)
    public void decreaseFavoriteCount() {
        if (this.favoriteCount > 0) this.favoriteCount--;
    }

    // 게시글 일부 정보 수정
    public void patchBoard(PatchBoardRequestDto dto) {
        this.title = dto.getTitle();
        this.content = dto.getContent();
    }

}
