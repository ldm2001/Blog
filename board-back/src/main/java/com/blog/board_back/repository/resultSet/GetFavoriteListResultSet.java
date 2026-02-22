package com.blog.board_back.repository.resultSet;

// 게시글에 좋아요한 유저 목록 조회 결과를 위한 인터페이스
public interface GetFavoriteListResultSet {
  
  String getEmail(); // 유저 이메일 반환
  String getNickname(); // 유저 닉네임 반환
  String getProfileImage(); // 유저 프로필 이미지 반환
}