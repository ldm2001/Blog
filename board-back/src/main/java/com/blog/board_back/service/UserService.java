package com.blog.board_back.service;

import org.springframework.http.ResponseEntity;

import com.blog.board_back.dto.response.user.GetSignInUserResponseDto;

public interface UserService {
  
  ResponseEntity<? super GetSignInUserResponseDto> getSingInUser(String email);

}
