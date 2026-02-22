package com.blog.board_back.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;    
import org.springframework.data.jpa.repository.Query;          
import org.springframework.stereotype.Repository;             

import com.blog.board_back.entity.SearchLogEntity;             
import com.blog.board_back.repository.resultSet.GetPopularListResultSet;
import com.blog.board_back.repository.resultSet.GetRelationListResultSet; 

@Repository // 인터페이스가 Repository임을 명시
public interface SearchLogRepository extends JpaRepository<SearchLogEntity, Integer> {

    // 인기 검색어 TOP 15를 조회하는 Native SQL 쿼리
    @Query(
        value = 
        "SELECT search_word as searchWord, count(search_word) AS count " + // 검색어, 카운트 추출
        "FROM search_log " + // 검색 로그 테이블
        "WHERE relation IS FALSE " + // 연관검색어 제외
        "GROUP BY search_word " + // 검색어별 그룹화
        "ORDER BY count DESC " + // 카운트 내림차순 정렬
        "LIMIT 15; " , // 상위 15개만 추출
        nativeQuery = true
    )
    List<GetPopularListResultSet> getPopularList(); // 인터페이스 반환

    @Query(
        value = 
        "SELECT relation_word as searchWord, count(relation_word) AS count " +
        "FROM search_log " +
        "WHERE search_word = ?1 " +
        "AND relation_word IS NOT NULL " +
        "GROUP BY relation_word " +
        "ORDER BY count DESC " +
        "LIMIT 15 ",
        nativeQuery = true
    )
    List<GetRelationListResultSet> getRelationList(String searchWord);

}
