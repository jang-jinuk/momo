package com.momo.momopjt.user.find;

import lombok.extern.log4j.Log4j2;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;


@Service
@Log4j2
public class EmailService {

  private final JavaMailSender mailSender;

  public EmailService(JavaMailSender mailSender) {
    this.mailSender = mailSender;
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
      log.error("Failed to send temporary password email to {}", userEmail, e);
    }
  }


}