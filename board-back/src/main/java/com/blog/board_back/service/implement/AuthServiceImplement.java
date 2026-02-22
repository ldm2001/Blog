package com.blog.board_back.service.implement;

import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.blog.board_back.dto.request.auth.RefreshTokenRequestDto;
import com.blog.board_back.dto.request.auth.SignInRequestDto;
import com.blog.board_back.dto.request.auth.SignUpRequestDto;
import com.blog.board_back.dto.response.ResponseDto;
import com.blog.board_back.dto.response.auth.RefreshTokenResponseDto;
import com.blog.board_back.dto.response.auth.SignInResponseDto;
import com.blog.board_back.dto.response.auth.SignUpResponseDto;
import com.blog.board_back.entity.UserEntity;
import com.blog.board_back.provider.JwtProvider;
import com.blog.board_back.repository.UserRepository;
import com.blog.board_back.service.AuthService;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service // 서비스 빈 등록
@RequiredArgsConstructor // final 멤버를 파라미터로 받는 생성자 자동 생성
@Transactional(readOnly = true)
public class AuthServiceImplement implements AuthService {

  private static final Logger log = LoggerFactory.getLogger(AuthServiceImplement.class);

  // 유저 레포지토리 의존성 주입
  private final UserRepository userRepository;

  // JWT 토큰 생성/검증 제공자 의존성 주입
  private final JwtProvider jwtProvider;

  // 비밀번호 암호화 인코더
  private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

  // 회원가입 비즈니스 로직
  @Override
  @Transactional
  public ResponseEntity<? super SignUpResponseDto> signUp(SignUpRequestDto dto) {
    try {
      String email = dto.getEmail();
      boolean existedEmail = userRepository.existsByEmail(email); // 이메일 중복 검사
      if (existedEmail) return SignUpResponseDto.duplicateEmail();

      String nickname = dto.getNickname();
      boolean existedNickname = userRepository.existsByNickname(nickname); // 닉네임 중복 검사
      if (existedNickname) return SignUpResponseDto.duplicateNickname();

      String telNumber = dto.getTelNumber();
      boolean existedTelNumber = userRepository.existsByTelNumber(telNumber); // 전화번호 중복 검사
      if (existedTelNumber) return SignUpResponseDto.duplicateTelNumber();

      String password = dto.getPassword();
      String encodedPassword = passwordEncoder.encode(password); // 비밀번호 암호화
      dto.setPassword(encodedPassword);

      UserEntity userEntity = new UserEntity(dto); // 엔티티 생성
      userRepository.save(userEntity); // DB 저장

    } catch (Exception exception) {
        log.error("AuthService error", exception);
        return ResponseDto.databaseError(); // DB 오류 처리
    }
    return SignUpResponseDto.success(); // 성공 응답 반환
  }

  // 로그인 비즈니스 로직 (AccessToken + RefreshToken 발급)
  @Override
  @Transactional
  public ResponseEntity<? super SignInResponseDto> signIn(SignInRequestDto dto) {

    String token = null;
    String refreshToken = null;

    try {
        String email = dto.getEmail();
        UserEntity userEntity = userRepository.findByEmail(email); // 사용자 조회
        if (userEntity == null) return SignInResponseDto.signInFail();

        String password = dto.getPassword();
        String encodedPassword = userEntity.getPassword();
        boolean isMatched = passwordEncoder.matches(password, encodedPassword); // 비밀번호 일치 확인
        if (!isMatched) return SignInResponseDto.signInFail();

        token = jwtProvider.create(email);              // AccessToken 생성 (15분)
        refreshToken = jwtProvider.createRefreshToken(); // RefreshToken 생성 (UUID)
        userEntity.setRefreshToken(refreshToken);        // DB에 RefreshToken 저장
        userRepository.save(userEntity);

    } catch (Exception exception) {
        log.error("AuthService error", exception);
        return ResponseDto.databaseError(); // DB 오류 처리
    }
    return SignInResponseDto.success(token, refreshToken); // 성공 시 두 토큰 반환
  }

  // AccessToken 갱신 비즈니스 로직 (Refresh Token Rotation)
  @Override
  @Transactional
  public ResponseEntity<? super RefreshTokenResponseDto> refreshToken(RefreshTokenRequestDto dto) {
    try {
        // DB에서 Refresh Token으로 사용자 조회
        UserEntity userEntity = userRepository.findByRefreshToken(dto.getRefreshToken());
        if (userEntity == null) return RefreshTokenResponseDto.invalidRefreshToken();

        // 새 AccessToken + 새 RefreshToken 발급 (회전 방식)
        String newToken = jwtProvider.create(userEntity.getEmail());
        String newRefreshToken = jwtProvider.createRefreshToken();
        userEntity.setRefreshToken(newRefreshToken); // 기존 RefreshToken 교체
        userRepository.save(userEntity);

        return RefreshTokenResponseDto.success(newToken, newRefreshToken);

    } catch (Exception exception) {
        log.error("AuthService refreshToken error", exception);
        return ResponseDto.databaseError();
    }
  }

}
