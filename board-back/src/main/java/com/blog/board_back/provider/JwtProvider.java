package com.blog.board_back.provider;

import java.security.Key;                        
import java.time.Instant;                        
import java.time.temporal.ChronoUnit;           
import java.util.Base64;                         
import java.util.Date;                           

import org.springframework.beans.factory.annotation.Value; 
import org.springframework.stereotype.Component;           

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component // 스프링 빈으로 등록
public class JwtProvider {

    private static final Logger log = LoggerFactory.getLogger(JwtProvider.class);

    // JWT 서명 비밀키를 application.properties에서 주입받음
    @Value("${secret-key}")
    private String secretKey;

    // JWT AccessToken 생성 (15분 만료)
    public String create(String email) {

        // 1. 만료일 생성 (15분)
        Date expireDate = Date.from(Instant.now().plus(15, ChronoUnit.MINUTES));
        // 2. 비밀키 디코딩 및 서명용 키 객체 생성
        Key key = Keys.hmacShaKeyFor(Base64.getDecoder().decode(secretKey));

        // 3. JWT 토큰 빌드 및 반환
        String jwt = Jwts.builder()
            .signWith(key, SignatureAlgorithm.HS256) // HMAC SHA-256
            .setSubject(email) // subject(주제)로 이메일 세팅
            .setIssuedAt(new Date()) // 토큰 발급 일시
            .setExpiration(expireDate) // 토큰 만료 일시
            .compact();

        return jwt;
    
    }

    // JWT 토큰 유효성 검증 및 이메일 추출
    public String validate(String jwt) {

        Claims claims = null; // JWT Claims 담을 변수
        Key key = Keys.hmacShaKeyFor(Base64.getDecoder().decode(secretKey)); // 서명용 키 생성

        try {
            // JWT 파서 빌드 및 검증
            claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(jwt)
                .getBody();
        } catch (Exception exception) {
            // 유효하지 않은 토큰일 경우 경고 로그 및 null 반환
            log.warn("Invalid or expired JWT token", exception);
            return null;
        }

        // 유효하다면 subject(이메일) 반환
        return claims.getSubject();

    }
}
