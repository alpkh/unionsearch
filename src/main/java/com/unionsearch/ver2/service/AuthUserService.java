package com.unionsearch.ver2.service;

import com.unionsearch.ver2.dto.LoginRequest;
import com.unionsearch.ver2.dto.SignupRequest;
import com.unionsearch.ver2.dto.ForgotPasswordRequest;
import com.unionsearch.ver2.entity.User;
import com.unionsearch.ver2.entity.VerificationCode;
import com.unionsearch.ver2.repository.UserRepository;
import com.unionsearch.ver2.repository.VerificationCodeRepository;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
public class AuthUserService implements UserDetailsService {

  private final UserRepository userRepository;
  private final VerificationCodeRepository verificationCodeRepository;
  private final PasswordEncoder passwordEncoder;
  private final JavaMailSender mailSender;

  public AuthUserService(UserRepository userRepository, VerificationCodeRepository verificationCodeRepository,
      PasswordEncoder passwordEncoder, JavaMailSender mailSender) {
    this.userRepository = userRepository;
    this.verificationCodeRepository = verificationCodeRepository;
    this.passwordEncoder = passwordEncoder;
    this.mailSender = mailSender;
  }

  // UserDetailsService 구현을 통한 사용자 로드
  @Override
  public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
    User user = userRepository.findByEmail(email)
        .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));

    return new org.springframework.security.core.userdetails.User(
        user.getEmail(),
        user.getPassword(),
        new ArrayList<>());
  }

  // 로그인 처리
  public Map<String, Object> login(LoginRequest request) {
    Optional<User> userOptional = userRepository.findByEmail(request.getEmail());

    Map<String, Object> response = new HashMap<>();
    if (userOptional.isEmpty()) {
      response.put("status", "fail");
      response.put("message", "가입 정보가 없습니다.");
      return response;
    }

    User user = userOptional.get();
    if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
      response.put("status", "fail");
      response.put("message", "이메일 또는 비밀번호가 잘못되었습니다.");
      return response;
    }

    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
        user.getEmail(), null, new ArrayList<>());
    SecurityContextHolder.getContext().setAuthentication(authenticationToken);

    response.put("status", "success");
    response.put("message", "로그인 성공");
    response.put("username", user.getName());
    return response;
  }

  public Authentication getAuthentication() {
    return SecurityContextHolder.getContext().getAuthentication();
  }

  // 로그인 상태 확인
  public Map<String, Object> checkLogin() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    Map<String, Object> response = new HashMap<>();
    if (authentication != null && !(authentication instanceof AnonymousAuthenticationToken)) {
      response.put("isLoggedIn", true);
      response.put("username", authentication.getName());
      return response;
    } else {
      response.put("isLoggedIn", false);
      return response;
    }
  }

  // 로그아웃 처리
  public void logout() {
    SecurityContextHolder.clearContext();
  }

  // 임시 비밀번호 생성
  private String generateTemporaryPassword() {
    return UUID.randomUUID().toString().substring(0, 8);
  }

  // 비밀번호 찾기
  public void forgotPassword(ForgotPasswordRequest request) {
    User user = userRepository.findByEmail(request.getEmail())
        .orElseThrow(() -> new RuntimeException("이메일을 찾을 수 없습니다."));

    String temporaryPassword = generateTemporaryPassword();
    user.setPassword(passwordEncoder.encode(temporaryPassword));

    userRepository.save(user);

    sendEmail(user.getEmail(), "임시 비밀번호 안내",
        "안녕하세요,\n\n요청하신 임시 비밀번호는 다음과 같습니다:\n\n" + temporaryPassword +
            "\n\n로그인 후 비밀번호를 변경해 주세요.\n\n감사합니다.");
  }

  // 회원가입 처리
  public void signup(SignupRequest request) {
    if (userRepository.existsByEmail(request.getEmail())) {
      throw new IllegalArgumentException("이미 가입된 이메일입니다.");
    }

    User user = new User();
    user.setEmail(request.getEmail());
    user.setName(request.getName());
    user.setPassword(passwordEncoder.encode(request.getPassword()));

    userRepository.save(user);
  }

  public boolean checkEmailExists(String email) {
    return userRepository.existsByEmail(email);
  }

  // 인증 코드 생성
  private String generateVerificationCode() {
    return UUID.randomUUID().toString().substring(0, 6);
  }

  // 이메일 전송
  private void sendEmail(String toEmail, String subject, String text) {
    SimpleMailMessage message = new SimpleMailMessage();
    message.setTo(toEmail);
    message.setSubject(subject);
    message.setText(text);
    try {
      mailSender.send(message);
    } catch (Exception e) {
      throw new RuntimeException("이메일 전송 중 문제가 발생했습니다.");
    }
  }

  // 이메일 인증 코드 전송
  public String sendVerificationCode(String email) {
    if (userRepository.existsByEmail(email)) {
      throw new IllegalArgumentException("이미 사용 중인 이메일입니다.");
    }

    String verificationCode = generateVerificationCode();
    sendEmail(email, "회원가입 인증 코드", "인증 코드: " + verificationCode + "\n해당 코드를 입력하여 인증을 완료해 주세요.");

    VerificationCode code = new VerificationCode(email, verificationCode);
    verificationCodeRepository.save(code);

    return "인증 코드가 이메일로 전송되었습니다.";
  }

  // 이메일 인증 코드 검증
  @Transactional
  public void verifyEmail(String email, String verificationCode) {
    VerificationCode code = verificationCodeRepository.findByEmail(email)
        .orElseThrow(() -> new IllegalArgumentException("인증 코드가 존재하지 않습니다. 이메일을 다시 확인해 주세요."));

    if (!code.getCode().equals(verificationCode)) {
      throw new IllegalArgumentException("잘못된 인증 코드입니다. 정확한 코드를 입력해 주세요.");
    }

    verificationCodeRepository.deleteByEmail(email);
  }

}
