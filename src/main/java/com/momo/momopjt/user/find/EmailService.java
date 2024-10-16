package com.momo.momopjt.user.find;

import lombok.extern.log4j.Log4j2;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Properties;

@Service
@Log4j2
public class EmailService {

  private final JavaMailSender mailSender;

  public EmailService(JavaMailSender mailSender) {
    this.mailSender = mailSender;
  }

  @PostConstruct
  private void configureMailSender() {
    if (mailSender instanceof JavaMailSenderImpl) {
      JavaMailSenderImpl javaMailSender = (JavaMailSenderImpl) mailSender;
      Properties props = javaMailSender.getJavaMailProperties();
      props.put("mail.smtp.auth", "true");
      props.put("mail.smtp.starttls.enable", "true");
      props.put("mail.smtp.starttls.required", "true");
      props.put("mail.smtp.ssl.protocols", "TLSv1.2");
      props.put("mail.debug", "true");  // Enable mail debugging
    }
  }

  public void sendTemporaryPasswordEmail(String userEmail, String temporaryPassword) {
    System.setProperty("https.protocols", "TLSv1.2,TLSv1.3");
    try {
      SimpleMailMessage message = new SimpleMailMessage();
      message.setTo(userEmail);
      message.setSubject("MOMO-임시 비밀번호입니다.");
      message.setText("임시 비밀번호로 로그인 후 비밀번호를 변경해주세요: " + temporaryPassword);
      mailSender.send(message);
      log.info("Temporary password email sent to {}", userEmail);
    } catch (Exception e) {
      log.error("Failed to send temporary password email to {}", userEmail, e);
      log.error("Exception message: {}", e.getMessage());
      for (StackTraceElement element : e.getStackTrace()) {
        log.error(element.toString());
      }
    }
  }
}
