package com.blog.board_back.config;

import java.io.IOException;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.annotation.web.configurers.HttpBasicConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain; 
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.blog.board_back.filter.JwtAuthenticationFilter;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Configuration  // 스프링 설정 클래스로 등록
@EnableWebSecurity  // Spring Security 활성화 어노테이션
@RequiredArgsConstructor  // final로 선언된 필드의 생성자 자동 생성
public class WebSecurityConfig {
  
  // JWT 사용자 인증 및 인가 처리
  private final JwtAuthenticationFilter jwtAuthenticationFilter;

  // Spring Security 보안 체인 설정
  @Bean
  protected SecurityFilterChain configure(HttpSecurity httpSecurity) throws Exception {
      
    httpSecurity
        // CORS 설정 적용 
        .cors(cors -> cors
            .configurationSource(corsConfigurationSource())
        )
        // CSRF 방지 비활성화 
        .csrf(CsrfConfigurer::disable)
        // HTTP Basic 인증 비활성화 
        .httpBasic(HttpBasicConfigurer::disable)
        // 세션 사용하지 않고 JWT 방식의 stateless 인증 사용
        .sessionManagement(sessionManagement -> sessionManagement
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        )
        // API별 접근 권한 설정
        .authorizeHttpRequests(request -> request
            // 인증 없이 접근 허용: 루트, 인증 관련, 검색, 파일 다운로드 엔드포인트
            .requestMatchers("/", "/api/v1/auth/**", "/api/v1/search/**", "/file/**", "/api/v1/ai/**").permitAll()
            // GET 방식에 한해 게시판/유저 일부 API 인증 없이 허용
            .requestMatchers(HttpMethod.GET, "/api/v1/board/**", "/api/v1/user/*").permitAll()
            // 위에서 지정한 것 이외의 모든 요청은 인증 필요
            .anyRequest().authenticated()
        )
        // 인증 실패시 커스텀 처리 
        .exceptionHandling(exceptionHandling -> exceptionHandling
            .authenticationEntryPoint(new FailedAuthenticationEntryPoint())
        )
        // Username/Password 필터 앞에 JWT 인증 필터 추가
        .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        
    return httpSecurity.build();  // 최종 SecurityFilterChain 반환
  }

  // CORS 세부 설정
  @Bean
  protected CorsConfigurationSource corsConfigurationSource() {
    
    CorsConfiguration configuration = new CorsConfiguration();
      // 프론트 도메인 명시
      configuration.addAllowedOrigin("http://localhost:3000");
      configuration.addAllowedOrigin("http://localhost:3001");
      // 모든 HTTP 메서드 허용 
      configuration.addAllowedMethod("*"); 
      // 모든 헤더 허용
      configuration.addAllowedHeader("*");
      // 자격 포함 허용
      configuration.setAllowCredentials(true);
      // Authorization 헤더 노출
      configuration.addExposedHeader("Authorization");

      // 모든 경로에 적용
      UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
      source.registerCorsConfiguration("/**", configuration);

      return source;
  }

}

// 인증 실패시 응답을 커스텀 처리하는 클래스
class FailedAuthenticationEntryPoint implements AuthenticationEntryPoint {

  // 인증 실패 시 실행되는 메서드 
  @Override
  public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
      throws IOException, ServletException {
    
        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);  // 401 코드 반환
        // 간단한 에러 메시지와 코드 전달
        response.getWriter().write("{ \"code\": \"AF\", \"message\": \"Authorization Failed\"}");
  
  }
  
}
