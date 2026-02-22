package com.blog.board_back.entity;

import java.text.SimpleDateFormat;        
import java.time.Instant;                 
import java.util.Date;                    

import com.blog.board_back.dto.request.board.PostCommentRequestDto; 

import jakarta.persistence.Entity;         
import jakarta.persistence.GeneratedValue; 
import jakarta.persistence.GenerationType; 
import jakarta.persistence.Id;             
import jakarta.persistence.Table;          
import lombok.AllArgsConstructor;        
import lombok.Getter;                     
import lombok.NoArgsConstructor;          

@Getter // 모든 필드의 getter 메서드 자동 생성
@AllArgsConstructor // 모든 필드를 파라미터로 받는 생성자 자동 생성
@NoArgsConstructor // 파라미터 없는 기본 생성자 자동 생성
@Entity(name="comment") // JPA 엔티티 이름 지정
@Table(name="comment")  // DB 테이블명 지정
public class CommentEntity {

  // 댓글 고유 번호
  @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
  private int commentNumber;
  // 댓글 본문 내용
  private String content;
  // 댓글 작성 일시
  private String writeDatetime;
  // 댓글 작성자 이메일
  private String userEmail;
  // 댓글이 달린 게시글 번호
  private int boardNumber;

  // 댓글 등록 시, DTO와 게시글 번호, 이메일을 받아 엔티티를 초기화하는 생성자
  public CommentEntity(PostCommentRequestDto dto, Integer boardNumber, String email) {

    // 현재 시간 형식의 문자열로 변환
    Date now = Date.from(Instant.now());
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    String writeDatetime = simpleDateFormat.format(now);

    this.content = dto.getContent();
    this.writeDatetime = writeDatetime;
    this.userEmail = email;
    this.boardNumber = boardNumber;
  }
  
}
