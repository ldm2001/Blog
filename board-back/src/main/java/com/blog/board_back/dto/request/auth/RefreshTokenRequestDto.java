package com.blog.board_back.dto.request.auth;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// AccessToken 갱신 요청 DTO
@Getter
@Setter
@NoArgsConstructor
public class RefreshTokenRequestDto {
    @NotBlank // 유효성 검증
    private String refreshToken; // 갱신에 사용할 Refresh Token
}
