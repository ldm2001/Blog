package com.blog.board_back.dto.request.auth;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PasswordConfirmRequestDto {
  @NotBlank
  private String password;
}
