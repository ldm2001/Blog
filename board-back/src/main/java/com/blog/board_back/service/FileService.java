package com.blog.board_back.service;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

// 파일 처리 관련 서비스 인터페이스
public interface FileService { 
  String upload(MultipartFile file); // 파일 업로드 처리
  Resource getImage(String fileName); // 파일명으로 이미지 리소스 반환
}