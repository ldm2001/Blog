package com.blog.board_back.service.implement;

import java.io.File;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.blog.board_back.service.FileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service // 서비스 컴포넌트 등록
public class FileServiceImplement implements FileService {

  private static final Logger log = LoggerFactory.getLogger(FileServiceImplement.class);

  // 파일 저장 경로 
  @Value("${file.path}")
  private String filePath;
  // 파일 접근 URL 
  @Value("${file.url}")
  private String fileUrl;

  // 파일 업로드 처리
  @Override
  public String upload(MultipartFile file) {
    
    // 업로드 파일이 비어있으면 null 반환
    if (file.isEmpty()) return null;

    // 원본 파일명과 확장자 추출
    String originalFileName = file.getOriginalFilename();
    if (originalFileName == null || originalFileName.isBlank()) return null;
    String extension = originalFileName.substring(originalFileName.lastIndexOf("."));
    // uuid로 새 파일명 생성
    String uuid = UUID.randomUUID().toString();
    String saverFileName = uuid + extension;
    String savePath = filePath + saverFileName;

    try {
        // 파일 저장
        file.transferTo(new File(savePath));
    } catch (Exception exception) {
        log.error("FileService error", exception);
        // 저장 중 에러 발생시 null 반환
        return null;
    }

    // 저장된 파일의 URL 반환
    String url = fileUrl + saverFileName;
    return url;

  }

  // 이미지 파일 리소스 반환
  @Override
  public Resource getImage(String fileName) {

    // Path Traversal 방어: ../ 또는 절대경로 포함 시 null 반환
    if (fileName == null || fileName.contains("..") || fileName.contains("/") || fileName.contains("\\")) {
        return null;
    }

    Resource resource = null;

    try {
        // 파일 경로를 리소스 객체로 변환
        resource = new UrlResource("file:" + filePath + fileName);
    } catch (Exception exception) {
        log.error("FileService error", exception);
        // 실패시 null 반환
        return null;
    }

    return resource;

  }
  
}
