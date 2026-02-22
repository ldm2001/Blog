package com.blog.board_back.service.implement;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.blog.board_back.dto.request.user.PatchNicknameRequestDto;
import com.blog.board_back.dto.request.user.PatchProfileImageRequestDto;
import com.blog.board_back.dto.response.ResponseDto;
import com.blog.board_back.dto.response.user.GetSignInUserResponseDto;
import com.blog.board_back.dto.response.user.GetUserBoardListResponseDto;
import com.blog.board_back.dto.response.user.GetUserResponseDto;
import com.blog.board_back.dto.response.user.PatchNicknameResponseDto;
import com.blog.board_back.dto.response.user.PatchProfileImageResponseDto;
import com.blog.board_back.entity.BoardListViewEntity;
import com.blog.board_back.entity.UserEntity;
import com.blog.board_back.repository.BoardListViewRepository;
import com.blog.board_back.repository.UserRepository;
import com.blog.board_back.service.UserService;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service // 서비스 빈 등록
@RequiredArgsConstructor // 생성자 자동 생성
@Transactional(readOnly = true)
public class UserServiceImplement implements UserService {

  private static final Logger log = LoggerFactory.getLogger(UserServiceImplement.class);

  // User 엔티티를 위한 레포지토리
  private final UserRepository userRepository;
  // 게시글 목록 뷰 레포지토리
  private final BoardListViewRepository boardListViewRepository;

  // 로그인한 유저 정보 조회
  @Override
  public ResponseEntity<? super GetSignInUserResponseDto> getSignInUser(String email) {

    UserEntity userEntity = null;

    try {
      // 이메일로 유저 엔티티 조회
      userEntity = userRepository.findByEmail(email);

      // 없는 유저면 401
      if (userEntity == null)
        return GetSignInUserResponseDto.notExistUser();

    } catch (Exception exception) {
        log.error("UserService error", exception);
        // DB 오류 응답 반환
        return ResponseDto.databaseError();
    }

    // 조회 성공 시 200 및 유저 정보 반환
    return GetSignInUserResponseDto.success(userEntity);

  }

  // 특정 유저 프로필 조회
  @Override
  public ResponseEntity<? super GetUserResponseDto> getUser(String userEmail) {

    UserEntity userEntity = null;

    try {
      // 이메일로 유저 엔티티 조회
      userEntity = userRepository.findByEmail(userEmail);

      // 없는 유저면 400
      if (userEntity == null)
        return GetUserResponseDto.notExistUser();

    } catch (Exception exception) {
      exception.printStackTrace();
      return ResponseDto.databaseError();
    }

    return GetUserResponseDto.success(userEntity);
  }

  // 유저가 작성한 게시글 목록 조회
  @Override
  public ResponseEntity<? super GetUserBoardListResponseDto> getUserBoardList(String userEmail) {

    List<BoardListViewEntity> boardListViewEntities = null;

    try {
      // 유저 존재 여부 확인
      boolean isExistUser = userRepository.existsByEmail(userEmail);
      if (!isExistUser)
        return GetUserResponseDto.notExistUser();

      // 해당 유저의 게시글 목록 조회
      boardListViewEntities = boardListViewRepository.findByWriterEmailOrderByWriteDatetimeDesc(userEmail);

    } catch (Exception exception) {
      exception.printStackTrace();
      return ResponseDto.databaseError();
    }

    return GetUserBoardListResponseDto.success(boardListViewEntities);
  }

  // 닉네임 수정
  @Override
  @Transactional
  public ResponseEntity<? super PatchNicknameResponseDto> patchNickname(PatchNicknameRequestDto requestBody, String email) {

    try {
      // 로그인한 유저 조회
      UserEntity userEntity = userRepository.findByEmail(email);
      if (userEntity == null)
        return PatchNicknameResponseDto.notExistUser();

      // 닉네임 중복 확인
      String nickname = requestBody.getNickname();
      boolean isExistNickname = userRepository.existsByNickname(nickname);
      if (isExistNickname)
        return PatchNicknameResponseDto.duplicateNickname();

      // 닉네임 업데이트 후 저장
      userEntity.setNickname(nickname);
      userRepository.save(userEntity);

    } catch (Exception exception) {
      exception.printStackTrace();
      return ResponseDto.databaseError();
    }

    return PatchNicknameResponseDto.success();
  }

  // 프로필 이미지 수정
  @Override
  @Transactional
  public ResponseEntity<? super PatchProfileImageResponseDto> patchProfileImage(PatchProfileImageRequestDto requestBody, String email) {

    try {
      // 로그인한 유저 조회
      UserEntity userEntity = userRepository.findByEmail(email);
      if (userEntity == null)
        return PatchProfileImageResponseDto.notExistUser();

      // 프로필 이미지 업데이트 후 저장
      String profileImage = requestBody.getProfileImage();
      userEntity.setProfileImage(profileImage);
      userRepository.save(userEntity);

    } catch (Exception exception) {
      exception.printStackTrace();
      return ResponseDto.databaseError();
    }

    return PatchProfileImageResponseDto.success();
  }

}
