package com.blog.board_back.dto.request.ai;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

// 클라이언트에서 보낼 JSON 바디의 구조를 정의한 DTO
@Getter
@Setter
public class SuggestTitleRequestDto {
    @NotBlank(message = "content는 비어 있을 수 없습니다.")
    private String content;
}
