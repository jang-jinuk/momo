package com.momo.momopjt.user.find;

import com.momo.momopjt.user.User;
import com.momo.momopjt.user.UserController;
import com.momo.momopjt.user.UserRepository;
import com.momo.momopjt.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/user")
@RequiredArgsConstructor
@Log4j2
public class FindController {

  private final UserService userService;
  private final UserRepository userRepository;
  private final EmailService emailService;

  @GetMapping("/find-id")
  public String showFindIdForm(Model model) {
    model.addAttribute("findUserIdRequest", new FindUserIdRequest());
    return "user/find/id";
  }

  // 아이디 찾기
  @PostMapping("/userid")
  public String findUserId(@ModelAttribute("findUserIdRequest") FindUserIdRequest findUserIdRequest, Model model) {
    String userEmail = findUserIdRequest.getFindUserEmail();
    String userId = userService.findUsernameByEmail(userEmail);
    if (userId != null) {
      model.addAttribute("userId", userId);
      log.info("Found userId: {}", userId); // 유저 아이디 찾음을 로그
    } else {
      model.addAttribute("errorMessageUserId", "User ID not found for email: " + userEmail);
      log.warn("User ID not found for email: {}", userEmail); // 아이디를 못 찾음을 경고 로그
    }
    return "user/find/id";
  }

  // 비밀번호 찾기 폼 렌더링
  @GetMapping("/find-pw")
  public String showFindPasswordForm(Model model) {
    model.addAttribute("findPasswordRequest", new FindPasswordRequest());
    return "user/find/pw";
  }

  // 비밀번호 찾기 처리
  @PostMapping("/find/password")
  public String findPassword(@ModelAttribute("findPasswordRequest") FindPasswordRequest findPasswordRequest,
                             RedirectAttributes redirectAttributes) {
    log.info("----------------- [POST find password]-----------------");
    String userId = findPasswordRequest.getUserId();
    String userEmail = findPasswordRequest.getUserEmail();

    try {
      User user = userService.findByUserIdAndUserEmail(userId, userEmail);

      if (user != null) {
        String temporaryPassword = userService.generateTemporaryPassword();
        log.info("----------------- [0716 YY 임시비번 ]-----------------{}",temporaryPassword);
        userService.updateUserPassword(user, temporaryPassword);
        emailService.sendTemporaryPasswordEmail(userEmail, temporaryPassword);
        redirectAttributes.addFlashAttribute("resetPasswordMessage", "임시 비밀번호가 이메일로 전송되었습니다.");
        log.info("Temporary password sent to email: {}", userEmail);

        return "redirect:/home";
      } else {
        redirectAttributes.addFlashAttribute("errorMessage", "해당 사용자 아이디와 이메일을 찾을 수 없습니다.");
        log.warn("User not found for username: {} and email: {}", userId, userEmail);
      }
    } catch (Exception e) {
      redirectAttributes.addFlashAttribute("errorMessage", "요청 처리 중 오류가 발생했습니다. 다시 시도해 주세요.");
      log.error("Error during password reset request processing", e);
    }
    return "redirect:/find-pw"; // 오류 발생 시 올바른 경로로 리디렉트
  }

  // 비밀번호 재설정 처리 (토큰 없이)
  @GetMapping("/reset/userPw")
  public String showResetPasswordForm(Model model) {
    model.addAttribute("resetPasswordRequest", new ResetPasswordRequest());
    return "user/find/resetPassword";
  }

  // 비밀번호 재설정 처리 (토큰 없이)
  @PostMapping("/reset/userPw")
  public String resetPassword(@ModelAttribute("resetPasswordRequest") ResetPasswordRequest resetPasswordRequest,
                              RedirectAttributes redirectAttributes,
                              Model model) {
    String userId = resetPasswordRequest.getUserId();
    String newPassword = resetPasswordRequest.getPassword();

    // 비밀번호 일치 검사
    if (!newPassword.equals(resetPasswordRequest.getConfirmPassword())) {
      model.addAttribute("passwordMismatchError", "비밀번호가 일치하지 않습니다.");
      model.addAttribute("resetPasswordRequest", resetPasswordRequest);
      return "user/find/resetPassword";
    }

    try {
      boolean resetSuccess = userService.resetPasswordByUserId(userId, newPassword);

      if (resetSuccess) {
        redirectAttributes.addFlashAttribute("resetSuccessMessage", "비밀번호가 성공적으로 재설정되었습니다!");
        return "redirect:/user/home"; // 여기서 경로 변경
      } else {
        redirectAttributes.addFlashAttribute("errorMessage", "비밀번호 재설정에 실패했습니다. 다시 시도해 주세요.");
        return "redirect:/reset";
      }
    } catch (Exception e) {
      redirectAttributes.addFlashAttribute("errorMessage", "요청 처리 중 오류가 발생했습니다. 다시 시도해 주세요.");
      log.error("Error during password reset", e);
      return "redirect:/reset";
    }
  }


  // 예외 처리 핸들러
  @ExceptionHandler(Exception.class)
  public String handleException(Exception e, RedirectAttributes redirectAttributes) {
    redirectAttributes.addFlashAttribute("errorMessage", "예상치 못한 오류가 발생했습니다. 다시 시도해 주세요.");
    log.error("Unhandled exception", e);
    return "redirect:/find-pw";
  }
}
