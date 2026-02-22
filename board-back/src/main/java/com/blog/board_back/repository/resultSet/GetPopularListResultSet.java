package com.blog.board_back.repository.resultSet;

// 인기 검색어 조회 결과를 위한 인터페이스
public interface GetPopularListResultSet {
    
    String getSearchWord(); // 인기 검색어 반환
    int getCount(); // 해당 검색어의 검색 횟수 반환
}