package com.momo.momopjt.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
//import org.springframework.mail.javamail.JavaMailSender;
//import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

//@Configuration
public class EmailConfig {

//  @Bean
//  public JavaMailSender javaMailSender() {
//    JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
//    mailSender.setHost("smtp.gmail.com");
//    mailSender.setPort(587);
//    mailSender.setUsername("dbwjd4689@gmail.com");
//    mailSender.setPassword("qidb dbjs qxpx mgrg");

//    Properties props = mailSender.getJavaMailProperties();
//    props.put("mail.transport.protocol", "smtp"); //이메일 전송에 사용될 프로토콜을 설정
//    props.put("mail.smtp.auth", "true");//SMTP 서버에 인증을 사용할지 여부를 설정
//    props.put("mail.smtp.starttls.enable", "true");//SMTP 통신에서 STARTTLS를
//    // 사용하여 보안 연결을 활성화할지 여부를 설정합니다. 대개는 "true"로 설정하여 보안 연결을 사용
//    props.put("mail.debug", "true"); //디버그 모드를 활성화할지 여부를 설정합니다.
//    // "true"로 설정하면 JavaMail 라이브러리의 디버그 로그가 출력
//
//    return mailSender;
//  }
}