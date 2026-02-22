package com.blog.board_back.repository;

import org.springframework.data.jpa.repository.JpaRepository;    
import org.springframework.stereotype.Repository;             

import com.blog.board_back.entity.UserEntity;                

@Repository // 인터페이스가 Repository임을 명시
public interface UserRepository extends JpaRepository<UserEntity, String> {

  // 이메일 중복 여부 확인
  boolean existsByEmail(String email);

  // 닉네임 중복 여부 확인
  boolean existsByNickname(String nickname);

  // 전화번호 중복 여부 확인
  boolean existsByTelNumber(String telNumber);

  // 이메일로 사용자 엔티티 조회
  UserEntity findByEmail(String email);

  // Refresh Token으로 사용자 엔티티 조회
  UserEntity findByRefreshToken(String refreshToken);

}
