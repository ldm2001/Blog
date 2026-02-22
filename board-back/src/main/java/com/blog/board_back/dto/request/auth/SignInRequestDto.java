package com.blog.board_back.dto.request.auth;

import jakarta.validation.constraints.NotBlank; 
import lombok.Getter;             
import lombok.NoArgsConstructor;  
import lombok.Setter;             

@Getter // 모든 필드의 getter 메서드 자동 생성
@Setter // 모든 필드의 setter 메서드 자동 생성
@NoArgsConstructor // 기본 생성자(파라미터 없음) 자동 생성
public class SignInRequestDto {
  @NotBlank // 유효성 검증
  private String email; // 로그인할 때 사용할 이메일
  @NotBlank // 유효성 검증
  private String password; // 로그인할 때 사용할 비밀번호
}
