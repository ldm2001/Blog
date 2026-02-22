package com.blog.board_back.entity;

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
@Entity(name="image") // JPA 엔티티 이름 지정
@Table(name="image")  // DB 테이블명 지정
public class ImageEntity {
    
    // 이미지 고유 시퀀스 번호
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int sequence; 
    // 이미지가 속한 게시글 번호
    private int boardNumber;
    // 이미지 파일명 또는 URL
    private String image;

    // 게시글 번호와 이미지 파일명으로 엔티티를 초기화하는 생성자
    public ImageEntity(int boardNumber, String image) {
      this.boardNumber = boardNumber;
      this.image = image;
    }

}
