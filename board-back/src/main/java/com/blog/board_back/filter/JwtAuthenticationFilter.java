package com.blog.board_back.filter;

import java.io.IOException;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.lang.NonNull;

import com.blog.board_back.provider.JwtProvider;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component // 스프링 빈으로 등록
@RequiredArgsConstructor // final 필드 자동 주입
public class JwtAuthenticationFilter extends OncePerRequestFilter {

  private static final Logger log = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

  // JWT 검증/추출
  private final JwtProvider jwtProvider;

  // 요청당 한 번씩 동작
  @Override
  protected void doFilterInternal(
      @NonNull HttpServletRequest request,
      @NonNull HttpServletResponse response,
      @NonNull FilterChain filterChain
  ) throws ServletException, IOException {

    try {
      // 1. Authorization 헤더에서 Bearer 토큰 추출
      String token = parseBearerToken(request);

      // 2. 토큰이 없으면 인증 없이 다음 필터 진행
      if (token == null) {
        filterChain.doFilter(request, response);
        return;
      }

      // 3. 토큰 유효성 검사 및 이메일 추출
      String email = jwtProvider.validate(token);

      // 4. 검증 실패 시 인증 없이 다음 필터 진행
      if (email == null) {
        filterChain.doFilter(request, response);
        return;
      }

      // 5. 인증 객체 생성
      AbstractAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(email, null,
          AuthorityUtils.NO_AUTHORITIES);
      authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

      // 6. SecurityContext에 인증 객체 등록
      SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
      securityContext.setAuthentication(authenticationToken);

      SecurityContextHolder.setContext(securityContext);
    } catch (Exception exception) {
      // 예외 발생 시 에러 로그 출력
      log.error("JWT authentication filter error", exception);
    }

    // 마지막에 반드시 다음 필터로 요청을 넘김
    filterChain.doFilter(request, response);
  }

  // Authorization 헤더에서 Bearer 토큰 추출하는 메서드
  private String parseBearerToken(HttpServletRequest request) {

    // 1. Authorization 헤더 값 가져오기
    String authorization = request.getHeader("Authorization");

    // 2. 헤더 값이 비었으면 null 반환
    boolean hasAuthorization = StringUtils.hasText(authorization);
    if (!hasAuthorization)
      return null;

    // 3. Bearer 타입인지 확인
    boolean isBearer = authorization.startsWith("Bearer ");
    if (!isBearer)
      return null;

    // 4. Bearer 이후의 실제 토큰만 추출해서 반환
    String token = authorization.substring(7);
    return token;
  }

}
