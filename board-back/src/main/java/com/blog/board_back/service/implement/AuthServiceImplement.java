package com.blog.board_back.service.implement;

import java.time.LocalDateTime;
import java.util.Locale;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.blog.board_back.dto.request.auth.SignInRequestDto;
import com.blog.board_back.dto.request.auth.SignUpRequestDto;
import com.blog.board_back.dto.response.ResponseDto;
import com.blog.board_back.dto.response.auth.RefreshTokenResponseDto;
import com.blog.board_back.dto.response.auth.SignInResponseDto;
import com.blog.board_back.dto.response.auth.SignUpResponseDto;
import com.blog.board_back.entity.UserEntity;
import com.blog.board_back.provider.JwtProvider;
import com.blog.board_back.provider.RefreshTokenProvider;
import com.blog.board_back.repository.UserRepository;
import com.blog.board_back.service.AuthService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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
  // Refresh Token 생성/해시/쿠키 제공자
  private final RefreshTokenProvider refreshTokenProvider;

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
      userRepository.saveAndFlush(userEntity); // DB 저장 및 제약 즉시 검증

    } catch (DataIntegrityViolationException exception) {
        log.warn("AuthService signUp integrity violation", exception);
        return getSignUpIntegrityViolationResponse(exception);
    } catch (Exception exception) {
        log.error("AuthService error", exception);
        return ResponseDto.databaseError(); // DB 오류 처리
    }
    return SignUpResponseDto.success(); // 성공 응답 반환
  }

  // 로그인 비즈니스 로직 (AccessToken + RefreshToken 발급)
  @Override
  @Transactional
  public ResponseEntity<? super SignInResponseDto> signIn(SignInRequestDto dto, HttpServletResponse response) {

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
        refreshToken = refreshTokenProvider.create();   // RefreshToken 생성 (UUID)
        storeRefreshToken(userEntity, refreshToken);    // DB에 RefreshToken 해시 + 만료 시각 저장
        refreshTokenProvider.addRefreshTokenCookie(response, refreshToken);

    } catch (Exception exception) {
        log.error("AuthService error", exception);
        return ResponseDto.databaseError(); // DB 오류 처리
    }
    return SignInResponseDto.success(token); // 성공 시 AccessToken 반환
  }

  // AccessToken 갱신 비즈니스 로직 (Refresh Token Rotation)
  @Override
  @Transactional
  public ResponseEntity<? super RefreshTokenResponseDto> refreshToken(HttpServletRequest request, HttpServletResponse response) {
    try {
        String refreshToken = refreshTokenProvider.extractRefreshToken(request);
        if (refreshToken == null) {
          refreshTokenProvider.expireRefreshTokenCookie(response);
          return RefreshTokenResponseDto.invalidRefreshToken();
        }

        String encodedRefreshToken = refreshTokenProvider.hash(refreshToken);

        // DB에서 Refresh Token으로 사용자 조회
        Optional<UserEntity> optionalUserEntity = userRepository.findByRefreshTokenForUpdate(encodedRefreshToken);
        UserEntity userEntity = optionalUserEntity.orElse(null);
        if (userEntity == null) {
          refreshTokenProvider.expireRefreshTokenCookie(response);
          return RefreshTokenResponseDto.invalidRefreshToken();
        }

        LocalDateTime refreshTokenExpiresAt = userEntity.getRefreshTokenExpiresAt();
        boolean isExpiredRefreshToken = refreshTokenExpiresAt == null || refreshTokenExpiresAt.isBefore(LocalDateTime.now());
        if (isExpiredRefreshToken) {
          clearRefreshToken(userEntity);
          refreshTokenProvider.expireRefreshTokenCookie(response);
          return RefreshTokenResponseDto.invalidRefreshToken();
        }

        // 새 AccessToken + 새 RefreshToken 발급 (회전 방식)
        String newToken = jwtProvider.create(userEntity.getEmail());
        String newRefreshToken = refreshTokenProvider.create();
        storeRefreshToken(userEntity, newRefreshToken); // 기존 RefreshToken 교체
        refreshTokenProvider.addRefreshTokenCookie(response, newRefreshToken);

        return RefreshTokenResponseDto.success(newToken);

    } catch (Exception exception) {
        log.error("AuthService refreshToken error", exception);
        return ResponseDto.databaseError();
    }
  }

  // 로그아웃 비즈니스 로직 (Refresh Token 서버 폐기)
  @Override
  @Transactional
  public ResponseEntity<ResponseDto> signOut(HttpServletRequest request, HttpServletResponse response) {
    try {
      String refreshToken = refreshTokenProvider.extractRefreshToken(request);

      if (refreshToken != null) {
        String encodedRefreshToken = refreshTokenProvider.hash(refreshToken);
        Optional<UserEntity> optionalUserEntity = userRepository.findByRefreshTokenForUpdate(encodedRefreshToken);
        UserEntity userEntity = optionalUserEntity.orElse(null);

        if (userEntity != null) clearRefreshToken(userEntity);
      }

      refreshTokenProvider.expireRefreshTokenCookie(response);
      return ResponseDto.successResponse();
    } catch (Exception exception) {
      log.error("AuthService signOut error", exception);
      return ResponseDto.databaseError();
    }
  }

  private void storeRefreshToken(UserEntity userEntity, String refreshToken) {
    String encodedRefreshToken = refreshTokenProvider.hash(refreshToken);
    LocalDateTime refreshTokenExpiresAt = refreshTokenProvider.getExpirationDateTime();

    userEntity.setRefreshToken(encodedRefreshToken);
    userEntity.setRefreshTokenExpiresAt(refreshTokenExpiresAt);
    userRepository.saveAndFlush(userEntity);
  }

  private void clearRefreshToken(UserEntity userEntity) {
    userEntity.setRefreshToken(null);
    userEntity.setRefreshTokenExpiresAt(null);
    userRepository.saveAndFlush(userEntity);
  }

  private ResponseEntity<? super SignUpResponseDto> getSignUpIntegrityViolationResponse(DataIntegrityViolationException exception) {
    String message = getRootMessage(exception);

    if (message.contains("uk_user_nickname")) return SignUpResponseDto.duplicateNickname();
    if (message.contains("uk_user_tel_number")) return SignUpResponseDto.duplicateTelNumber();
    if (message.contains("primary")) return SignUpResponseDto.duplicateEmail();

    return ResponseDto.databaseError();
  }

  private String getRootMessage(Throwable throwable) {
    Throwable root = throwable;

    while (root.getCause() != null) root = root.getCause();

    String message = root.getMessage();
    if (message == null) return "";

    return message.toLowerCase(Locale.ROOT);
  }

}
