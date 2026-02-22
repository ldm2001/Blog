package com.blog.board_back.dto.request.board;

import java.util.List;

import jakarta.validation.constraints.NotBlank; 
import jakarta.validation.constraints.NotNull; 
import lombok.Getter;             
import lombok.NoArgsConstructor;  
import lombok.Setter;             

@Getter // 모든 필드의 getter 메서드 자동 생성
@Setter // 모든 필드의 setter 메서드 자동 생성
@NoArgsConstructor // 파라미터 없는 기본 생성자 자동 생성
public class PatchBoardRequestDto {
    // 수정할 게시글 제목: 반드시 입력
    @NotBlank
    private String title;

    // 수정할 게시글 내용: 반드시 입력
    @NotBlank
    private String content;

    // 게시글의 이미지 리스트: 반드시 입력
    @NotNull
    private List<String> boardImageList;
}
