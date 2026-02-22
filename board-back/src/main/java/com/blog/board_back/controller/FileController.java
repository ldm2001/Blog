package com.blog.board_back.controller;

import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.blog.board_back.service.FileService;

import lombok.RequiredArgsConstructor; 

@RestController // REST API 
@RequestMapping("/file") // 모든 파일 관련 엔드포인트에 /file 접두사
@RequiredArgsConstructor // final 필드 생성자 자동 주입
public class FileController {
  
  // 파일 업로드/다운로드 비즈니스 로직 처리 서비스 DI
  private final FileService fileService;

  // 파일 업로드 처리 
  @PostMapping("/upload")
  public String upload(
    @RequestParam("file") MultipartFile file // 업로드된 파일을 파라미터로 받음
  ) {
    String url = fileService.upload(file); // 파일 저장 후 접근 가능한 URL 반환
    return url;
  }

  // 이미지 파일 다운로드/조회 처리 
  @GetMapping(value="/{fileName}", produces={MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_PNG_VALUE})
  public Resource getImage(
    @PathVariable("fileName") String fileName // 요청 경로에서 파일명 추출
  ) {
    Resource resource = fileService.getImage(fileName); // 파일을 리소스 형태로 반환
    return resource;
  }

}
