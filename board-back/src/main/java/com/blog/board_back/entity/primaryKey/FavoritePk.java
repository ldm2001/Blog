package com.blog.board_back.entity.primaryKey;

import java.io.Serializable; 
import jakarta.persistence.Column; 
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter // 모든 필드의 getter 메서드 자동 생성
@Setter // 모든 필드의 setter 메서드 자동 생성
@NoArgsConstructor // 파라미터 없는 기본 생성자 자동 생성
@AllArgsConstructor // 모든 필드를 파라미터로 받는 생성자 자동 생성
@EqualsAndHashCode // JPA 복합키 동등성 비교를 위해 equals/hashCode 자동 생성
public class FavoritePk implements Serializable { // JPA PK 객체
    // 유저 이메일을 PK로 사용
    @Column(name="user_email")
    private String userEmail;
    // 게시글 번호를 PK로 사용
    @Column(name="board_number")
    private int boardNumber;
}
