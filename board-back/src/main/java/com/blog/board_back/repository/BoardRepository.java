package com.blog.board_back.repository;

import org.springframework.data.jpa.repository.JpaRepository;    
import org.springframework.data.jpa.repository.Query;          
import org.springframework.stereotype.Repository;            

import com.blog.board_back.entity.BoardEntity;                
import com.blog.board_back.repository.resultSet.GetBoardResultSet; 

@Repository // 인터페이스가 Repository임을 명시
public interface BoardRepository extends JpaRepository<BoardEntity, Integer> {

  // 게시글 번호로 존재 여부 확인
  boolean existsByBoardNumber(Integer boardNumber);

  // 게시글 번호로 게시글 단일 조회
  BoardEntity findByBoardNumber(Integer boardNumber);
  
  // 게시글 상세 조회 및 작성자 닉네임, 프로필을 함께 조회하는 Native SQL 쿼리
  @Query(
    value=
    "SELECT " +
    "B.board_number AS boardNumber, " + // 게시글 번호
    "B.title AS title, " + // 게시글 제목
    "B.content As content, " + // 게시글 본문 내용
    "B.write_datetime AS writeDatetime, " + // 작성일시
    "B.writer_email AS writerEmail, " + // 작성자 이메일
    "U.nickname AS writerNickname, " + // 작성자 닉네임
    "U.profile_image AS writerProfileImage " + // 작성자 프로필 이미지
    "FROM board AS B " + // 게시글 테이블
    "INNER JOIN user AS U " + // 유저 테이블과 조인
    "ON B.writer_email = U.email " + // 이메일 PK로 조인
    "WHERE board_number = ?1 " , // 게시글 번호 기준
    nativeQuery=true
  )
  GetBoardResultSet getBoard(Integer boardNumber); // 인터페이스 반환

}
