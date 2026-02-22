package com.blog.board_back.dto.request.board;

import java.util.List;

import jakarta.validation.constraints.NotBlank; 
import jakarta.validation.constraints.NotNull;  
import lombok.Getter;             
import lombok.NoArgsConstructor;  
import lombok.Setter;             

@Setter // 모든 필드의 setter 메서드 자동 생성
@Getter // 모든 필드의 getter 메서드 자동 생성
@NoArgsConstructor // 파라미터 없는 기본 생성자 자동 생성
public class PostBoardRequestDto {

  // 게시글 제목
  @NotBlank
  private String title;

  // 게시글 본문 내용
  @NotBlank
  private String content;

  // 게시글 이미지 리스트
  @NotNull
  private List<String> boardImageList;
  
}
