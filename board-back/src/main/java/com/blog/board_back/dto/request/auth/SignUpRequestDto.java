package com.blog.board_back.dto.request.auth;

import jakarta.validation.constraints.AssertTrue; 
import jakarta.validation.constraints.Email;      
import jakarta.validation.constraints.NotBlank;   
import jakarta.validation.constraints.NotNull;    
import jakarta.validation.constraints.Pattern;    
import jakarta.validation.constraints.Size;       
import lombok.Getter;             
import lombok.NoArgsConstructor;  
import lombok.Setter;             

@NoArgsConstructor // 기본 생성자 자동 생성
@Setter // 모든 필드의 setter 메서드 자동 생성
@Getter // 모든 필드의 getter 메서드 자동 생성
public class SignUpRequestDto {
  
    // 이메일 필드: 이메일 형식이어야 함
    @NotBlank @Email
    private String email;

    // 비밀번호 필드: 8~20자 길이
    @NotBlank @Size(min=8, max=20)
    private String password;

    // 닉네임 필드: 반드시 입력
    @NotBlank 
    private String nickname;

    // 전화번호 필드: 11~13자리 숫자만 허용
    @NotBlank @Pattern(regexp="^[0-9]{11,13}$")
    private String telNumber;

    // 기본 주소: 반드시 입력
    @NotBlank
    private String address;

    // 상세 주소: 선택
    private String addressDetail;

    // 개인정보 동의 체크: true여야 함
    @NotNull @AssertTrue
    private Boolean agreedPersonal;

}

