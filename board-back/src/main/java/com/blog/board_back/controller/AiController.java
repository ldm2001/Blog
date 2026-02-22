package com.blog.board_back.controller;

import com.blog.board_back.dto.request.ai.SuggestTitleRequestDto;
import com.blog.board_back.dto.response.ai.SuggestTitleResponseDto;
import com.blog.board_back.service.AiService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

// AiService를 통해 Gemini api를 호출하여 제목을 추천하여 반환
@RestController
@RequestMapping("/api/v1/ai")
public class AiController {

    private final AiService aiService;
    
        public AiController(AiService aiService) {
            this.aiService = aiService;
        }

    @PostMapping("/suggest-title")
    public ResponseEntity<SuggestTitleResponseDto> suggestTitle(
            @Valid @RequestBody SuggestTitleRequestDto dto) {
        return aiService.suggestTitle(dto);
    }
}

