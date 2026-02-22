package com.blog.board_back.repository.resultSet;

// 게시글 상세 조회 결과를 위한 인터페이스
public interface GetBoardResultSet {
  
  Integer getBoardNumber(); // 게시글 고유 번호 반환
  String getTitle(); // 게시글 제목 반환
  String getContent(); // 게시글 내용 반환
  String getWriteDatetime();  // 게시글 작성 일시 반환
  String getWriterEmail(); // 작성자 이메일 반환
  String getWriterNickname(); // 작성자 닉네임 반환
  String getWriterProfileImage(); // 작성자 프로필 이미지 반환
}