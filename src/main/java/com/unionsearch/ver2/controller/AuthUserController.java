package com.unionsearch.ver2.controller;

import com.unionsearch.ver2.dto.SignupRequest;
import com.unionsearch.ver2.dto.ForgotPasswordRequest;
import com.unionsearch.ver2.service.AuthUserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthUserController {

  private final AuthUserService authUserService;

  public AuthUserController(AuthUserService authUserService) {
    this.authUserService = authUserService;
  }

  // 로그인 상태 확인 엔드포인트
  @GetMapping("/check-login")
  public ResponseEntity<Map<String, Object>> checkLogin() {
    Map<String, Object> response = authUserService.checkLogin();
    return ResponseEntity.ok(response);
  }

  // 로그아웃 엔드포인트
  @PostMapping("/logout")
  public ResponseEntity<Map<String, String>> logout(HttpServletRequest request, HttpServletResponse response) {
    new SecurityContextLogoutHandler().logout(request, response, authUserService.getAuthentication());
    authUserService.logout();
    Map<String, String> responseMap = Map.of("status", "success", "message", "로그아웃 성공");
    return ResponseEntity.ok(responseMap);
  }

  // 회원가입 엔드포인트
  @PostMapping("/signup")
  public ResponseEntity<Map<String, String>> signup(@RequestBody SignupRequest request) {
    Map<String, String> response = new HashMap<>();
    try {
      authUserService.signup(request);
      response.put("status", "success");
      response.put("message", "회원가입이 완료되었습니다.");
      return ResponseEntity.ok(response);
    } catch (IllegalArgumentException e) {
      response.put("status", "fail");
      response.put("message", e.getMessage());
      return ResponseEntity.badRequest().body(response);
    } catch (Exception e) {
      response.put("status", "error");
      response.put("message", "서버 오류가 발생했습니다.");
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
  }

  // 이메일 인증 코드 전송 엔드포인트
  @PostMapping("/send-verification-code")
  public ResponseEntity<Map<String, String>> sendVerificationCode(@RequestParam String email) {
    Map<String, String> response = new HashMap<>();
    if (!email.endsWith("@union-search.com") && !email.endsWith("@yonsei.ac.kr")) {
      response.put("status", "fail");
      response.put("message", "허용된 도메인 이메일만 사용 가능합니다.");
      return ResponseEntity.badRequest().body(response);
    }

    try {
      authUserService.sendVerificationCode(email);
      response.put("status", "success");
      response.put("message", "인증 코드가 이메일로 전송되었습니다.");
      return ResponseEntity.ok(response);
    } catch (IllegalArgumentException e) {
      response.put("status", "fail");
      response.put("message", e.getMessage());
      return ResponseEntity.badRequest().body(response);
    } catch (Exception e) {
      response.put("status", "error");
      response.put("message", "인증 코드 전송 중 서버 오류가 발생했습니다.");
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
  }

  // 이메일 인증 코드 검증 엔드포인트
  @PostMapping("/verify-code")
  public ResponseEntity<Map<String, String>> verifyCode(@RequestBody Map<String, String> request) {
    Map<String, String> response = new HashMap<>();
    String email = request.get("email");
    String code = request.get("code");

    if (email == null || code == null) {
      response.put("status", "fail");
      response.put("message", "이메일과 인증 코드는 모두 필수 항목입니다.");
      return ResponseEntity.badRequest().body(response);
    }

    try {
      authUserService.verifyEmail(email, code);
      response.put("status", "success");
      response.put("message", "이메일 인증이 완료되었습니다.");
      return ResponseEntity.ok(response);
    } catch (IllegalArgumentException e) {
      response.put("status", "fail");
      response.put("message", e.getMessage());
      return ResponseEntity.badRequest().body(response);
    } catch (Exception e) {
      response.put("status", "error");
      response.put("message", "이메일 인증 중 서버 오류가 발생했습니다.");
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
  }

  // 비밀번호 찾기 엔드포인트
  @PostMapping("/forgot-password")
  public ResponseEntity<Map<String, String>> forgotPassword(@RequestParam String email) {
    Map<String, String> response = new HashMap<>();
    try {
      ForgotPasswordRequest request = new ForgotPasswordRequest();
      request.setEmail(email);
      authUserService.forgotPassword(request);
      response.put("status", "success");
      response.put("message", "임시 비밀번호가 이메일로 전송되었습니다.");
      return ResponseEntity.ok(response);
    } catch (IllegalArgumentException e) {
      response.put("status", "fail");
      response.put("message", e.getMessage());
      return ResponseEntity.badRequest().body(response);
    } catch (Exception e) {
      response.put("status", "error");
      response.put("message", "비밀번호 찾기 중 서버 오류가 발생했습니다.");
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
  }

  // 이메일 중복 확인 엔드포인트
  @GetMapping("/check-email")
  public ResponseEntity<Map<String, Object>> checkEmailExists(@RequestParam String email) {
    Map<String, Object> response = new HashMap<>();
    try {
      boolean exists = authUserService.checkEmailExists(email);
      response.put("status", "success");
      response.put("exists", exists);
      response.put("message", exists ? "이미 사용 중인 이메일입니다." : "사용 가능한 이메일입니다.");
      return ResponseEntity.ok(response);
    } catch (Exception e) {
      response.put("status", "error");
      response.put("message", "이메일 중복 확인 중 서버 오류가 발생했습니다.");
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
  }
}
