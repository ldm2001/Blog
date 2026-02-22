package com.blog.board_back.repository.resultSet;

// 게시글의 댓글 목록 조회 결과를 위한 인터페이스
public interface GetCommentListResultSet {

  String getNickname(); // 댓글 작성자 닉네임 반환
  String getProfileImage(); // 댓글 작성자 프로필 이미지 반환
  String getWriteDatetime(); // 댓글 작성 일시 반환
  String getContent(); // 댓글 본문 내용 반환
}

