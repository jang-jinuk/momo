package com.momo.momopjt.user;

import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Random;


@Service
@Log4j2
public class EmailService {

  private static final Logger logger = LoggerFactory.getLogger(EmailService.class);

  private final JavaMailSender mailSender;

  public EmailService(JavaMailSender mailSender) {
    this.mailSender = mailSender;
  }

  public void sendTemporaryPasswordEmail(String userEmail, String temporaryPassword) {
    try {
      SimpleMailMessage message = new SimpleMailMessage();
      message.setTo(userEmail);
      message.setSubject("Your Temporary Password");
      message.setText("Your temporary password is: " + temporaryPassword);
      mailSender.send(message);
      logger.info("Temporary password email sent to {}", userEmail);
    } catch (Exception e) {
      logger.error("Failed to send temporary password email to {}", userEmail, e);
    }
  }

  private String generateRandomPassword() {
    // 임시 비밀번호를 랜덤하게 생성하는 코드 예시
    int length = 8; // 임시 비밀번호의 길이 설정
    String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    StringBuilder password = new StringBuilder();

    Random random = new Random();
    for (int i = 0; i < length; i++) {
      password.append(characters.charAt(random.nextInt(characters.length())));
    }

    return password.toString();
  }

  public void processForgotPassword(String userEmail) {
    String temporaryPassword = generateRandomPassword();
    sendTemporaryPasswordEmail(userEmail, temporaryPassword);
    // 추가적으로, 사용자의 비밀번호를 임시 비밀번호로 업데이트하는 로직을 여기에 추가할 수 있습니다.
  }
}