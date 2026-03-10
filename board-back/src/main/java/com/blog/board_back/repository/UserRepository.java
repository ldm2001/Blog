package com.blog.board_back.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;    
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;             

import com.blog.board_back.entity.UserEntity;                

import jakarta.persistence.LockModeType;

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

  // Refresh Token 행 잠금 조회
  @Lock(LockModeType.PESSIMISTIC_WRITE)
  @Query("select u from user u where u.refreshToken = :refreshToken")
  Optional<UserEntity> findByRefreshTokenForUpdate(@Param("refreshToken") String refreshToken);

}
