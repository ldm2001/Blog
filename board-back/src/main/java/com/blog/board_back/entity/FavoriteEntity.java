package com.blog.board_back.entity;

import com.blog.board_back.entity.primaryKey.FavoritePk; 

import jakarta.persistence.Entity;   
import jakarta.persistence.Id;       
import jakarta.persistence.IdClass;  
import jakarta.persistence.Table;    
import lombok.AllArgsConstructor;    
import lombok.Getter;                
import lombok.NoArgsConstructor;     

@Getter // 모든 필드의 getter 메서드 자동 생성
@NoArgsConstructor // 파라미터 없는 기본 생성자 자동 생성
@AllArgsConstructor // 모든 필드를 파라미터로 받는 생성자 자동 생성
@Entity(name="favorite")    // JPA 엔티티 이름 지정
@Table(name="favorite")     // DB 테이블명 지정
@IdClass(FavoritePk.class)  // PK 클래스 지정
public class FavoriteEntity {
    // 좋아요한 사용자 이메일 
    @Id 
    private String userEmail;
    // 좋아요 게시글 번호 
    @Id
    private int boardNumber;
}
