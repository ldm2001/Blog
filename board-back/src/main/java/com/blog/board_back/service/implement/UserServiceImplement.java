package com.blog.board_back.service.implement;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.blog.board_back.dto.response.ResponseDto;
import com.blog.board_back.dto.response.user.GetSignInUserResponseDto;
import com.blog.board_back.entity.UserEntity;
import com.blog.board_back.repository.UserRepository;
import com.blog.board_back.service.UserService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImplement implements UserService {

  private final UserRepository userRepository;
  
  @Override
  public ResponseEntity<? super GetSignInUserResponseDto> getSingInUser(String email) {

    UserEntity userEntity = null;
    
    try {

      userEntity = userRepository.findByEmail(email);
      if (userEntity == null) return GetSignInUserResponseDto.notExistUser();
      
    } catch (Exception exception) {
        exception.printStackTrace();
        return ResponseDto.databaseError();
    }

    return GetSignInUserResponseDto.success(userEntity);

  }
  
  
}
