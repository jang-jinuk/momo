package com.momo.momopjt.global.security.handler;


import com.momo.momopjt.user.UserSecurityDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Log4j2
@RequiredArgsConstructor
public class CustomSocialLoginSuccessHandler implements AuthenticationSuccessHandler {

  private final PasswordEncoder passwordEncoder;

  @Override
  public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                      Authentication authentication) throws IOException, ServletException {

    log.info("---------------------------------------");
    log.info("CustomLoginSuccessHandler onAuthenticationSuccess............");
    log.info(authentication.getPrincipal());
    UserSecurityDTO userSecurityDTO = (UserSecurityDTO) authentication.getPrincipal();

    String encodedPw = userSecurityDTO.getUserPw();

    // 소셜 로그인 여부를 'Y'로 확인하고, 패스워드가 "1111"인지 확인
    if (userSecurityDTO.getUserSocial() == 'Y'
        && (userSecurityDTO.getUserPw().equals("1111")
        || passwordEncoder.matches("1111", userSecurityDTO.getUserPw()))) {
      log.info("-------- [IF]-------you");

      log.info("Should Change Password");
      log.info("Redirect to User Modify");
      response.sendRedirect("/user/modify");
      /* 자동가입된  회원도 PasswordEncoder를 이용해서 1111을 인코딩한 상태이므로
      matches()를 이용해서 검사하고 결과에 따라 /user/modify로 보내거나 /user/home으로 리다이렉트 시킨다.
       */
      return;
    } else {
      response.sendRedirect("/user/home");
    }
    log.info("-------- [ELSE]-------you");
  }
}