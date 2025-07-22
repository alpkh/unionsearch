package com.unionsearch.ver2.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class CustomAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

  private final AuthenticationManager authenticationManager;
  private final AuthenticationFailureHandler authenticationFailureHandler;

  public CustomAuthenticationFilter(AuthenticationManager authenticationManager,
      AuthenticationFailureHandler authenticationFailureHandler) {
    this.authenticationManager = authenticationManager;
    this.authenticationFailureHandler = authenticationFailureHandler;
    setFilterProcessesUrl("/api/auth/login"); // JSON 로그인 요청을 받을 URL 설정
  }

  @Override
  public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
      throws AuthenticationException {
    try {
      Map<String, String> credentials = new ObjectMapper().readValue(request.getInputStream(),
          new TypeReference<Map<String, String>>() {
          });
      String email = credentials.get("email");
      String password = credentials.get("password");

      UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(email, password);

      return authenticationManager.authenticate(authToken);
    } catch (IOException e) {
      throw new RuntimeException("Failed to parse authentication request body", e);
    }
  }

  @Override
  protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
      Authentication authResult) throws IOException {
    response.setStatus(HttpServletResponse.SC_OK);
    response.setContentType("application/json");

    Map<String, String> successResponse = new HashMap<>();
    successResponse.put("message", "Authentication successful");
    successResponse.put("username", authResult.getName());

    new ObjectMapper().writeValue(response.getWriter(), successResponse);
  }

  @Override
  protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
      AuthenticationException failed) throws IOException, ServletException {
    authenticationFailureHandler.onAuthenticationFailure(request, response, failed);
  }
}
