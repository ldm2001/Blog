package com.blog.board_back.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;    
import org.springframework.data.jpa.repository.Query;          
import org.springframework.stereotype.Repository;            

import com.blog.board_back.entity.CommentEntity;              
import com.blog.board_back.repository.resultSet.GetCommentListResultSet; 

import jakarta.transaction.Transactional;                      

@Repository // 인터페이스가 Repository임을 명시
public interface CommentRepository extends JpaRepository<CommentEntity, Integer> {

  // 특정 게시글의 닉네임, 프로필, 작성일, 내용을 조회하는 Native SQL 쿼리
  @Query(
    value= 
    "SELECT " +
    "   U.nickname AS nickname, " + // 유저 닉네임
    "   U.profile_image AS profileImage, " + // 유저 프로필 이미지
    "   C.write_datetime AS writeDatetime, " + // 댓글 작성일
    "   C.content AS content " + // 댓글 본문 내용
    "FROM comment AS C " + // 댓글 테이블
    "INNER JOIN user AS U " + // 유저 테이블과 조인
    "ON C.user_email = U.email " + // PK로 조인
    "WHERE C.board_number = ?1 " + // 게시글 번호 기준
    "ORDER BY writeDatetime DESC; ", // 최신 댓글이 먼저
    nativeQuery=true 
  )
  List<GetCommentListResultSet> getCommentList(Integer boardNumber); // 인터페이스 반환

  // 특정 게시글에 해당하는 댓글 일괄 삭제 
  @Transactional
  void deleteByBoardNumber(Integer boardNumber);

}
