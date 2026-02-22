package com.blog.board_back.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.blog.board_back.entity.BoardListViewEntity;

import java.util.List;

@Repository // 인터페이스가 Repository임을 명시
public interface BoardListViewRepository extends JpaRepository<BoardListViewEntity, Integer> {

    // 작성일시 기준 내림차순 전체 게시글 목록 조회
    List<BoardListViewEntity> findByOrderByWriteDatetimeDesc();

    // 즐겨찾기 → 댓글수 → 조회수 → 작성일시 순으로 상위 3개 게시글 조회 (기간 제한)
    List<BoardListViewEntity> findTop3ByWriteDatetimeGreaterThanOrderByFavoriteCountDescCommentCountDescViewCountDescWriteDatetimeDesc(String writeDatetime);

    // 즐겨찾기 → 댓글수 → 조회수 → 작성일시 순으로 전체 중 상위 3개 조회 (기간 제한 없음)
    List<BoardListViewEntity> findTop3ByOrderByFavoriteCountDescCommentCountDescViewCountDescWriteDatetimeDesc();

    // 랜덤으로 3개 게시글 조회
    @Query(value = "SELECT * FROM board_list_view ORDER BY RAND() LIMIT 3", nativeQuery = true)
    List<BoardListViewEntity> findRandom3();

    // 제목 또는 내용에 검색어가 포함된 게시글을 작성일시 기준 내림차순 조회 (페이지네이션)
    Page<BoardListViewEntity> findByTitleContainsOrContentContainsOrderByWriteDatetimeDesc(String title, String content, Pageable pageable);

    // 특정 유저가 작성한 게시글을 작성일시 기준 내림차순 조회 (전체)
    List<BoardListViewEntity> findByWriterEmailOrderByWriteDatetimeDesc(String email);

    // 특정 유저가 작성한 게시글을 작성일시 기준 내림차순 조회 (페이지네이션)
    Page<BoardListViewEntity> findByWriterEmailOrderByWriteDatetimeDesc(String email, Pageable pageable);

}
