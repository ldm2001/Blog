package com.blog.board_back.provider;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HexFormat;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.util.WebUtils;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class RefreshTokenProvider {

  @Value("${auth.refresh-token.expiration-days}")
  private long refreshTokenExpirationDays;

  @Value("${auth.refresh-token.cookie-name}")
  private String refreshTokenCookieName;

  @Value("${auth.refresh-token.cookie-path}")
  private String refreshTokenCookiePath;

  @Value("${auth.refresh-token.cookie-secure}")
  private boolean refreshTokenCookieSecure;

  @Value("${auth.refresh-token.cookie-same-site}")
  private String refreshTokenCookieSameSite;

  public String create() {
    return UUID.randomUUID().toString();
  }

  public String hash(String refreshToken) {
    try {
      MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
      byte[] hash = messageDigest.digest(refreshToken.getBytes(StandardCharsets.UTF_8));
      return HexFormat.of().formatHex(hash);
    } catch (NoSuchAlgorithmException exception) {
      throw new IllegalStateException("SHA-256 algorithm is not available.", exception);
    }
  }

  public LocalDateTime getExpirationDateTime() {
    return LocalDateTime.now().plusDays(refreshTokenExpirationDays);
  }

  public String extractRefreshToken(HttpServletRequest request) {
    Cookie cookie = WebUtils.getCookie(request, refreshTokenCookieName);
    if (cookie == null) return null;

    String refreshToken = cookie.getValue();
    if (!StringUtils.hasText(refreshToken)) return null;

    return refreshToken;
  }

  public void addRefreshTokenCookie(HttpServletResponse response, String refreshToken) {
    ResponseCookie cookie = ResponseCookie.from(refreshTokenCookieName, refreshToken)
        .httpOnly(true)
        .secure(refreshTokenCookieSecure)
        .sameSite(refreshTokenCookieSameSite)
        .path(refreshTokenCookiePath)
        .maxAge(Duration.ofDays(refreshTokenExpirationDays))
        .build();

    response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
  }

  public void expireRefreshTokenCookie(HttpServletResponse response) {
    ResponseCookie cookie = ResponseCookie.from(refreshTokenCookieName, "")
        .httpOnly(true)
        .secure(refreshTokenCookieSecure)
        .sameSite(refreshTokenCookieSameSite)
        .path(refreshTokenCookiePath)
        .maxAge(Duration.ZERO)
        .build();

    response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
  }

}
