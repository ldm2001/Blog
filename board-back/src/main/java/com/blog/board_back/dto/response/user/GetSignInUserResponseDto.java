package com.blog.board_back.dto.response.user;

import org.springframework.http.HttpStatus;         
import org.springframework.http.ResponseEntity;    

import com.blog.board_back.common.ResponseCode;    
import com.blog.board_back.common.ResponseMessage; 
import com.blog.board_back.dto.response.ResponseDto; 
import com.blog.board_back.entity.UserEntity;        

import lombok.Getter; 

@Getter // 모든 필드의 getter 메서드 자동 생성
public class GetSignInUserResponseDto extends ResponseDto {

  // 이메일
  private String email;
  // 닉네임
  private String nickname;
  // 프로필 이미지
  private String profileImage;

  // 유저 엔티티를 받아서 DTO로 변환하는 private 생성자
  private GetSignInUserResponseDto(UserEntity userEntity) {
    super(ResponseCode.SUCCESS, ResponseMessage.SUCCESS); // 성공 코드/메시지 세팅
    this.email = userEntity.getEmail();
    this.nickname = userEntity.getNickname();
    this.profileImage = userEntity.getProfileImage();
  }

  // 로그인 유저 정보 조회 성공(200 OK) 응답 생성용 static 메서드
  public static ResponseEntity<GetSignInUserResponseDto> success(UserEntity userEntity) {
    GetSignInUserResponseDto result = new GetSignInUserResponseDto(userEntity); // DTO 생성
    return ResponseEntity.status(HttpStatus.OK).body(result); // 200 반환
  }

  // 유저가 존재하지 않을 때 실패 응답
  public static ResponseEntity<ResponseDto> notExistUser() {
    ResponseDto result = new ResponseDto(ResponseCode.NOT_EXISTED_USER, ResponseMessage.NOT_EXISTED_USER);
    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(result); // 401 반환
  }
  
}
