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

    // Check if it's a social login and password is "1111"
    if (userSecurityDTO.getUserSocial() == 'Y' && (passwordEncoder.matches("1111", encodedPw))) {
      log.info("-------- []-------you"+encodedPw);
      log.info("Should Change Password");
      log.info("Redirect to User update");
      // Redirect to /user/update
      response.sendRedirect(request.getContextPath() + "/user/update");
    } else {
      // Normal login flow
      response.sendRedirect(request.getContextPath() + "/user/home");
    }
  }

}