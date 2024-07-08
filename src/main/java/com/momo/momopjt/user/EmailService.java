package com.momo.momopjt.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

  private static final Logger logger = LoggerFactory.getLogger(EmailService.class);

  @Autowired
  private JavaMailSender mailSender;

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
}