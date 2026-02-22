package com.blog.board_back.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.blog.board_back.entity.FavoriteEntity;
import com.blog.board_back.entity.primaryKey.FavoritePk;
import com.blog.board_back.repository.resultSet.GetFavoriteListResultSet;

import jakarta.transaction.Transactional;   

@Repository // 인터페이스가 Repository임을 명시
public interface FavoriteRepository extends JpaRepository<FavoriteEntity, FavoritePk> {

  // 게시글 번호, 사용자 이메일로 좋아요 정보 단일 조회
  FavoriteEntity findByBoardNumberAndUserEmail(Integer boardNumber, String userEmail);

  // 게시글의 좋아요한 유저 목록을 조회하는 Native SQL 쿼리
  @Query(
    value=
    "SELECT " +
    "   U.email AS email, " + // 유저 이메일
    "   U.nickname AS nickname, " + // 유저 닉네임
    "   U.profile_image AS profileImage " + // 유저 프로필 이미지
    "FROM favorite AS F " + // 즐겨찾기 테이블
    "INNER JOIN user AS U " + // 유저 테이블과 조인
    "ON F.user_email = U.email " + // PK로 조인
    "WHERE F.board_number = ?1", // 게시글 번호 기준
    nativeQuery=true 
  )
  List<GetFavoriteListResultSet> getFavoriteList(Integer boardNumber); // 인터페이스 반환

  // 특정 게시글에 해당하는 좋아요 정보 일괄 삭제 
  @Transactional
  void deleteByBoardNumber(Integer boardNumber);  
}

