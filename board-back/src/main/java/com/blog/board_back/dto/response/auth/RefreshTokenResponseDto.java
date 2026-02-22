package com.blog.board_back.dto.response.auth;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.blog.board_back.common.ResponseCode;
import com.blog.board_back.common.ResponseMessage;
import com.blog.board_back.dto.response.ResponseDto;

import lombok.Getter;

// AccessToken 갱신 응답 DTO
@Getter
public class RefreshTokenResponseDto extends ResponseDto {

    // 새로 발급된 JWT AccessToken
    private String token;
    // AccessToken 만료 시간 (초, 15분 = 900)
    private int expirationTime;
    // 새로 발급된 Refresh Token (회전 방식)
    private String refreshToken;

    // private 생성자: 성공 응답 생성에만 사용
    private RefreshTokenResponseDto(String token, String refreshToken) {
        super(ResponseCode.SUCCESS, ResponseMessage.SUCCESS);
        this.token = token;
        this.expirationTime = 900; // 15분
        this.refreshToken = refreshToken;
    }

    // 토큰 갱신 성공 시 응답 객체 생성
    public static ResponseEntity<RefreshTokenResponseDto> success(String token, String refreshToken) {
        RefreshTokenResponseDto result = new RefreshTokenResponseDto(token, refreshToken);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    // Refresh Token 무효 시 응답 객체 생성 (401)
    public static ResponseEntity<ResponseDto> invalidRefreshToken() {
        ResponseDto result = new ResponseDto(ResponseCode.INVALID_REFRESH_TOKEN, ResponseMessage.INVALID_REFRESH_TOKEN);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(result);
    }

}
