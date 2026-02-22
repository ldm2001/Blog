package com.blog.board_back.entity;

import jakarta.persistence.Entity; 
import jakarta.persistence.Id;     
import jakarta.persistence.Table;  
import lombok.AllArgsConstructor; 
import lombok.Getter;             
import lombok.NoArgsConstructor;  

@Getter // 모든 필드의 getter 메서드 자동 생성
@NoArgsConstructor // 파라미터 없는 기본 생성자 자동 생성
@AllArgsConstructor // 모든 필드를 파라미터로 받는 생성자 자동 생성
@Entity(name="board_list_view") // JPA 엔티티 이름 지정
@Table(name="board_list_view")  // DB 뷰 또는 테이블명 지정
public class BoardListViewEntity {
  
    // 게시글 고유 번호
    @Id
    private int boardNumber;
    // 게시글 제목
    private String title;
    // 게시글 내용
    private String content;
    // 게시글 대표 이미지
    private String titleImage;
    // 게시글 조회수
    private int viewCount;
    // 좋아요 수
    private int favoriteCount;
    // 댓글 수
    private int commentCount;
    // 게시글 작성 일시
    private String writeDatetime;
    // 작성자 이메일
    private String writerEmail;
    // 작성자 닉네임
    private String writerNickname;
    // 작성자 프로필 이미지
    private String writerProfileImage;
    
}
