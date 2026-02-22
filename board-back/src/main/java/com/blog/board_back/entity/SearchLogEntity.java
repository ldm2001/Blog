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
@Entity(name="search_log") // JPA 엔티티 이름 지정
@Table(name="search_log")  // DB 테이블명 지정
public class SearchLogEntity {

    // 검색 로그 고유 시퀀스 번호
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int sequence;
    // 검색어
    private String searchWord;
    // 연관 검색어
    private String relationWord;
    // 연관 여부
    private boolean relation;

    // 검색어, 연관검색어, 연관여부로 엔티티를 초기화하는 생성자
    public SearchLogEntity(String seacrhWord, String relationWord, boolean relation) {
        this.searchWord = seacrhWord;
        this.relationWord = relationWord;
        this.relation = relation;
    }

}
