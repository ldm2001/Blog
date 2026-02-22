package com.blog.board_back.dto.response.ai;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * AI 호출 결과로 내려보낼 “추천 제목 리스트”를 담는 응답 DTO
 */
@Getter
@Setter
@NoArgsConstructor
public class SuggestTitleResponseDto {
    private List<String> suggestedTitles;

    public SuggestTitleResponseDto(List<String> suggestedTitles) {
        this.suggestedTitles = suggestedTitles;
    }
}

