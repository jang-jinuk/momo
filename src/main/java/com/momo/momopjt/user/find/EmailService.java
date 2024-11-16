package com.momo.momopjt.user.find;

import jakarta.annotation.PostConstruct;
import lombok.extern.log4j.Log4j2;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@Log4j2
public class EmailService {

  private final JavaMailSender mailSender;

  public EmailService(JavaMailSender mailSender) {
    this.mailSender = mailSender;
  }

  @PostConstruct
  private void configureMailSender() {
    if (mailSender instanceof JavaMailSenderImpl javaMailSender) {
      javaMailSender.getJavaMailProperties().putAll(Map.of(
          "mail.smtp.auth", "true",
          "mail.smtp.starttls.enable", "true",
          "mail.smtp.starttls.required", "true",
          "mail.smtp.ssl.protocols", "TLSv1.2,TLSv1.3",
          "mail.debug", "true"
      ));
    }
  }

  public void sendTemporaryPasswordEmail(String userEmail, String temporaryPassword) {
    try {
      SimpleMailMessage message = new SimpleMailMessage();
      message.setTo(userEmail);
      message.setSubject("MOMO-임시 비밀번호입니다.");
      message.setText("임시 비밀번호로 로그인 후 비밀번호를 변경해주세요: " + temporaryPassword);
      mailSender.send(message);
      log.info("Temporary password email sent to {}", userEmail);
    } catch (Exception e) {
      log.error("임시 비밀번호 이메일 발송 실패: {}", userEmail, e);
      throw new RuntimeException("이메일 발송 실패", e);
    }
  }
}