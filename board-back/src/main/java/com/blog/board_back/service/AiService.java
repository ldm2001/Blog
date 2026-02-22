package com.blog.board_back.service;

import com.blog.board_back.dto.request.ai.SuggestTitleRequestDto;
import com.blog.board_back.dto.response.ai.SuggestTitleResponseDto;
import org.springframework.http.ResponseEntity;

// AI 기능을 수행하기 위한 서비스 인터페이스
public interface AiService {
    ResponseEntity<SuggestTitleResponseDto> suggestTitle(SuggestTitleRequestDto dto);
}

