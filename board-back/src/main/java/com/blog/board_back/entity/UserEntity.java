package com.blog.board_back.entity;

import java.time.LocalDateTime;

import com.blog.board_back.dto.request.auth.SignUpRequestDto; 

import jakarta.persistence.Entity; 
import jakarta.persistence.Id;     
import jakarta.persistence.Table;  
import lombok.AllArgsConstructor; 
import lombok.Getter;             
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter // 모든 필드의 getter 메서드 자동 생성
@NoArgsConstructor // 파라미터 없는 기본 생성자 자동 생성
@AllArgsConstructor // 모든 필드를 파라미터로 받는 생성자 자동 생성
@Entity(name="user") // JPA 엔티티 이름 지정
@Table(name="user")  // DB 테이블명 지정
public class UserEntity {

    // 사용자 이메일
    @Id
    private String email;
    // 암호화된 비밀번호
    private String password;
    // 닉네임 수정
    // 사용자 닉네임
    @Setter
    private String nickname;
    // 사용자 전화번호
    private String telNumber;
    // 기본 주소
    private String address;
    // 상세 주소
    private String addressDetail;
    // 프로필 이미지 수정
    // 프로필 이미지
    @Setter
    private String profileImage;
    // 개인정보 동의 여부
    private boolean agreedPersonal;
    // Refresh Token 수정
    // Refresh Token (로그인 시 발급, nullable)
    @Setter
    private String refreshToken;
    // Refresh Token 서버 만료 시각
    @Setter
    private LocalDateTime refreshTokenExpiresAt;

    // 회원가입 요청 DTO로 엔티티를 초기화하는 생성자
    public UserEntity(SignUpRequestDto dto) {
        this.email = dto.getEmail();
        this.password = dto.getPassword();
        this.nickname = dto.getNickname();
        this.telNumber = dto.getTelNumber();
        this.address = dto.getAddress();
        this.addressDetail = dto.getAddressDetail();
        this.agreedPersonal = dto.getAgreedPersonal();
    }

}
