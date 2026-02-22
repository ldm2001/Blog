package com.blog.board_back.controller;

import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.blog.board_back.dto.response.search.GetPopularListResponseDto;
import com.blog.board_back.dto.response.search.GetRelationListResponseDto;
import com.blog.board_back.service.SearchService; 

import lombok.RequiredArgsConstructor; // final 필드 생성자 자동 생성

@RestController // REST API 
@RequestMapping(value="/api/v1/search") // 모든 엔드포인트에 /api/v1/search 접두사 적용
@RequiredArgsConstructor // 생성자 주입
public class SearchController {

    // 인기 검색어 등 검색 관련 서비스 의존성 주입
    private final SearchService searchService;
    
    // 인기 검색어 목록 조회 
    @GetMapping("/popular-list")
    public ResponseEntity<? super GetPopularListResponseDto> getPopularList() {
        ResponseEntity<? super GetPopularListResponseDto> response = searchService.getPopularList(); // 서비스로부터 인기 검색어 리스트 조회
        return response; // 결과 반환
    }

    // 연관 검색어 목록 조회
    @GetMapping("/{searchWord}/relation-list")
    public ResponseEntity<? super GetRelationListResponseDto> getRelationList(
        @PathVariable("searchWord") String searchWord
    ) {
        ResponseEntity<? super GetRelationListResponseDto> response = searchService.getRelationList(searchWord); // 서비스로부터 연관 검색어 리스트 조회
        return response; // 결과 반환
    }
}
