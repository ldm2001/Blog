package com.blog.board_back.service;

import org.springframework.http.ResponseEntity;
import com.blog.board_back.dto.response.search.GetPopularListResponseDto;
import com.blog.board_back.dto.response.search.GetRelationListResponseDto;

// 검색 관련 서비스 인터페이스
public interface SearchService {
    
    ResponseEntity<? super GetPopularListResponseDto> getPopularList(); // 인기 검색어 리스트 조회
    ResponseEntity<? super GetRelationListResponseDto> getRelationList(String searchWord);
}
