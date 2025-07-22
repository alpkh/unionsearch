package com.unionsearch.ver2.config;

import com.unionsearch.ver2.filter.CustomAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

  @Bean
  public AuthenticationFailureHandler authenticationFailureHandler() {
    return new SimpleUrlAuthenticationFailureHandler(); // 기본 실패 핸들러 사용
  }

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http, AuthenticationConfiguration authConfig)
      throws Exception {
    AuthenticationManager authenticationManager = authConfig.getAuthenticationManager();
    CustomAuthenticationFilter customAuthenticationFilter = new CustomAuthenticationFilter(authenticationManager,
        authenticationFailureHandler());
    customAuthenticationFilter.setFilterProcessesUrl("/api/auth/login");

    http
        .cors(cors -> cors.configurationSource(corsConfigurationSource())) // CORS 설정 활성화
        .csrf(csrf -> csrf.disable()) // CSRF 비활성화
        .authorizeHttpRequests(auth -> auth
            .requestMatchers(
                "/api/positions/create",
                "/api/positions/update/**",
                "/api/positions/delete/**",
                "/api/positions/file/download",
                "/api/positions/file/upload")
            .authenticated() // 인증 필요 엔드포인트
            .requestMatchers("/api/auth/**").permitAll() // 인증 관련 엔드포인트 모두 허용
            .anyRequest().permitAll()) // 다른 모든 요청은 인증 필요 없음
        .formLogin(form -> form
            .loginPage("/login") // 로그인 페이지 URL 설정 (프론트엔드와 일치)
            .loginProcessingUrl("/api/auth/login") // 로그인 처리 URL 설정
            .successHandler(savedRequestAwareAuthenticationSuccessHandler()) // 성공 핸들러
            .failureHandler(authenticationFailureHandler())) // 실패 핸들러 추가
        .logout(logout -> logout
            .logoutUrl("/api/auth/logout")
            .deleteCookies("JSESSIONID")
            .logoutSuccessUrl("/") // 로그아웃 후 홈 페이지로 리다이렉트
            .permitAll())
        .sessionManagement(session -> session
            .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED) // 필요한 경우에만 세션 생성
            .maximumSessions(100) // 최대 세션 수 제한
            .maxSessionsPreventsLogin(false)); // 최대 세션 수 초과 시 이전 세션 만료

    http.addFilterBefore(customAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

    return http.build();
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration configuration = new CorsConfiguration();
    configuration.setAllowedOrigins(Arrays.asList("*")); // 모든 도메인 허용
    configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
    configuration.setAllowedHeaders(Arrays.asList("*")); // 모든 헤더 허용
    configuration.setAllowCredentials(true); // 쿠키 전달 허용

    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", configuration);
    return source;
  }

  @Bean
  public SavedRequestAwareAuthenticationSuccessHandler savedRequestAwareAuthenticationSuccessHandler() {
    SavedRequestAwareAuthenticationSuccessHandler handler = new SavedRequestAwareAuthenticationSuccessHandler();
    handler.setDefaultTargetUrl("/"); // 로그인 성공 시 이동할 기본 URL 설정
    return handler;
  }
}
