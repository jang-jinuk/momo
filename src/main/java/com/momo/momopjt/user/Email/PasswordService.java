package com.momo.momopjt.user.Email;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class PasswordService {

  private final JavaMailSender mailSender;

  @Autowired
  public PasswordService(JavaMailSender mailSender) {
    this.mailSender = mailSender;
  }

  // 아이디 찾기 로직
  public String findUsernameByEmail(String email) {
    // 실제로는 데이터베이스에서 해당 이메일로 등록된 아이디를 조회하는 로직이 구현되어야 합니다.
    // 임시로 하드코딩된 예시를 사용합니다.
    if ("test@example.com".equals(email)) {
      return "testuser";
    } else {
      return null;
    }
  }

  // 비밀번호 재설정 로직
  public boolean resetPassword(String userId, String email, String newPassword) {
    // 실제로는 데이터베이스에서 userId와 email을 기준으로 사용자를 찾아 비밀번호를 업데이트하고, 이메일을 발송하는 로직이 구현되어야 합니다.
    // 여기서는 간단히 userId와 email이 일치하면 비밀번호를 업데이트하고 이메일을 발송하는 예시를 사용합니다.
    if ("testuser".equals(userId) && "test@example.com".equals(email)) {
      // 비밀번호 업데이트 로직 (여기서는 간단히 출력만)
      System.out.println("사용자 " + userId + "의 비밀번호를 " + newPassword + "으로 업데이트하였습니다.");

      // 비밀번호 재설정 이메일 발송
      sendResetPasswordEmail(email);

      return true;
    } else {
      return false;
    }
  }

  // 비밀번호 재설정 이메일 발송
  private void sendResetPasswordEmail(String email) {
    // 실제로는 이메일 전송 로직을 구현해야 합니다.
    // 여기서는 간단한 예시로 메시지를 콘솔에 출력합니다.
    System.out.println("비밀번호 재설정 이메일을 " + email + "로 발송하였습니다.");
  }
}