package com.blog.board_back.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;    
import org.springframework.stereotype.Repository;             

import com.blog.board_back.entity.ImageEntity;                

import jakarta.transaction.Transactional;                     

@Repository // 인터페이스가 Repository임을 명시
public interface ImageRepository extends JpaRepository<ImageEntity, Integer> {
  
  // boardNumber에 속한 이미지 리스트 조회
  List<ImageEntity> findByBoardNumber(Integer boardNumber);

  // 특정 게시글의 이미지 일괄 삭제 
  @Transactional
  void deleteByBoardNumber(Integer boardNumber);

}
