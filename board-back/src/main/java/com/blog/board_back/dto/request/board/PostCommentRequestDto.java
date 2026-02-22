package com.blog.board_back.dto.request.board;

import jakarta.validation.constraints.NotBlank; 
import lombok.Getter;             
import lombok.NoArgsConstructor;  
import lombok.Setter;             

@Getter // 모든 필드의 getter 메서드 자동 생성
@Setter // 모든 필드의 setter 메서드 자동 생성
@NoArgsConstructor // 파라미터 없는 기본 생성자 자동 생성
public class PostCommentRequestDto {

  // 댓글 본문 내용
  @NotBlank
  private String content;
  
}

