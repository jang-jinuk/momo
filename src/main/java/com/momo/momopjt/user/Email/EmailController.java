package com.momo.momopjt.user.Email;

import com.momo.momopjt.user.UserRepository;
import com.momo.momopjt.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class EmailController {
  private final PasswordService passwordService;
  //private final UserRepository userRepository;

  @Autowired
  public EmailController(PasswordService passwordService) {
    this.passwordService = passwordService;
  }

  // GET 요청 처리: 이메일 찾기 폼 제공
  @GetMapping("/user/find/{userEmail}")
  public String showFindUsernameForm() {
    return "user/findUsernameForm"; // 이메일 입력 폼 페이지를 반환
  }

  // POST 요청 처리: 이메일 찾기
  @PostMapping("/user/find/userEmail")
  public String findUsername(@ModelAttribute FindUsernameRequest request) {
    String email = request.getFindUsernameEmail();
    String userEmail = passwordService.findUsernameByEmail(email);
    if (userEmail != null) {
      // 아이디를 찾았을 때는 /user/home으로 리다이렉트
      return "redirect:/user/home";
    } else {
      // 사용자를 찾지 못했을 때는 다시 폼 페이지로 리다이렉트
      return "redirect:/user/find/userEmail";
    }
  }



  // POST 요청 처리: 비밀번호 재설정
  @PostMapping("/user/find/reset/userPw")
  public String resetPassword(@ModelAttribute ResetPasswordRequest request) {
    String userId = request.getResetPasswordUserId();
    String email = request.getResetPasswordEmail();

    // 임시 비밀번호 생성
    String newPassword = generateRandomPassword();

    boolean success = passwordService.resetPassword(userId, email, newPassword);
    if (success) {
      // 비밀번호 재설정 이메일을 발송했을 때는 /user/home으로 리다이렉트
      return "redirect:/user/home";
    } else {
      // 사용자를 찾지 못했을 때는 다시 폼 페이지로 리다이렉트
      return "redirect:/find/reset/userPw";
    }
  }

  // 임시 비밀번호 생성 메서드 예시
  private String generateRandomPassword() {
    // 임시 비밀번호 생성 로직
    // 예: 랜덤한 문자열 생성, 혹은 복잡한 패턴을 사용하여 생성
    return "tempPassword123"; // 실제 구현에서는 보안을 고려하여 안전한 방식으로 생성
  }
  }