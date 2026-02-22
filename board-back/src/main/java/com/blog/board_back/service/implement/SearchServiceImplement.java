package com.blog.board_back.service.implement;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.blog.board_back.dto.response.ResponseDto;
import com.blog.board_back.dto.response.search.GetPopularListResponseDto;
import com.blog.board_back.dto.response.search.GetRelationListResponseDto;
import com.blog.board_back.repository.SearchLogRepository;
import com.blog.board_back.repository.resultSet.GetPopularListResultSet;
import com.blog.board_back.repository.resultSet.GetRelationListResultSet;
import com.blog.board_back.service.SearchService;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service // 서비스 빈으로 등록
@RequiredArgsConstructor // 생성자 자동 주입
@Transactional(readOnly = true) // 모든 메서드 기본 읽기 전용 트랜잭션
public class SearchServiceImplement implements SearchService {

    private static final Logger log = LoggerFactory.getLogger(SearchServiceImplement.class);

    // 인기 검색어 조회용 레포지토리
    private final SearchLogRepository searchLogRepository;
    
    // 인기 검색어 리스트 조회 서비스
    @Override
    public ResponseEntity<? super GetPopularListResponseDto> getPopularList() {

        List<GetPopularListResultSet> resultSets = new ArrayList<>();

        try {
            // DB에서 인기 검색어 목록 조회
            resultSets = searchLogRepository.getPopularList();
            
        } catch (Exception exception) {
            log.error("SearchService error", exception);
            // DB 오류 시 500 
            return ResponseDto.databaseError();
        }

        // 성공 시 인기 검색어 리스트 반환
        return GetPopularListResponseDto.success(resultSets);
    }

    // 연관 검색어 리스트 조회 서비스
    @Override
    public ResponseEntity<? super GetRelationListResponseDto> getRelationList(String searchWord) {

        List<GetRelationListResultSet> resultSets = new ArrayList<>();

        try {
            // DB에서 연관 검색어 목록 조회
            resultSets = searchLogRepository.getRelationList(searchWord);

        } catch (Exception exception) {
            log.error("SearchService error", exception);
            // DB 오류 시 500
            return ResponseDto.databaseError();
        }

        // 성공 시 연관 검색어 리스트 반환
        return GetRelationListResponseDto.success(resultSets);

    }

}
