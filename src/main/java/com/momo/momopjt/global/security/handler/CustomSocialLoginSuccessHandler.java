package com.momo.momopjt.global.security.handler;


import com.momo.momopjt.user.User;
import com.momo.momopjt.user.UserRepository;
import com.momo.momopjt.user.UserSecurityDTO;
import com.momo.momopjt.user.UserRole;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.security.core.GrantedAuthority;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;
import java.util.Map;
import java.time.Instant;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

//@Service //불필요 ? 0716 YY
@Log4j2
@RequiredArgsConstructor
public class CustomSocialLoginSuccessHandler implements AuthenticationSuccessHandler {

  private final PasswordEncoder passwordEncoder;
  private final UserRepository userRepository;

  @Override
//  @Transactional // 불필요 ? 0716 YY
  public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                      Authentication authentication) throws IOException, ServletException {
    log.info("---------------------------------------");
    log.info("CustomLoginSuccessHandler onAuthenticationSuccess............");
    log.info(authentication.getPrincipal());

    Object principal = authentication.getPrincipal();
    UserSecurityDTO userSecurityDTO = null;
    String encodedPw = null;

    if (principal instanceof DefaultOAuth2User) {
      DefaultOAuth2User oAuth2User = (DefaultOAuth2User) principal;
      String provider = ((OAuth2AuthenticationToken) authentication).getAuthorizedClientRegistrationId();
      String id = null;
      String email = null;
      Character social = null;

      log.info("Should Change Password");
      log.info("Redirect to User update");
      response.sendRedirect("/user/update");
      /* 자동가입된  회원도 PasswordEncoder를 이용해서 1111을 인코딩한 상태이므로
      matches()를 이용해서 검사하고 결과에 따라 /user/modify로 보내거나 /user/home으로 리다이렉트 시킨다.
       */
      return ;
    } else {
      response.sendRedirect("/home");
    }
  }

    // 일반 로그인 흐름
    response.sendRedirect(request.getContextPath() + "/user/home");
  }

  private String getKakaoEmail(Map<String, Object> paramMap) {
    log.info("KAKAO -------------------------");

    Object account = paramMap.get("kakao_account");
    log.info("Kakao Account: {}", account);

    if (account instanceof Map) {
      Map<String, Object> accountMap = (Map<String, Object>) account;
      Object emailObj = accountMap.get("email");

      if (emailObj instanceof String) {
        String email = (String) emailObj;
        log.info("Kakao Email: {}", email);

        return email;
      }
    }

    return null;
  }

  private String getNaverEmail(Map<String, Object> paramMap) {
    log.info("NAVER -------------------------");

    Object response = paramMap.get("response");
    log.info("Naver Response: {}", response);

    if (response instanceof Map) {
      Map<String, Object> responseMap = (Map<String, Object>) response;
      Object emailObj = responseMap.get("email");
      if (emailObj instanceof String) {
        String email = (String) emailObj;
        log.info("Naver Email: {}", email);
        return email;
      }
    }
    return null;
  }

  private String getNaverId(Map<String, Object> paramMap) {
    log.info("NAVER ID -------------------------");

    Object response = paramMap.get("response");
    log.info("Naver Response: {}", response);

    if (response instanceof Map) {
      Map<String, Object> responseMap = (Map<String, Object>) response;
      Object idObj = responseMap.get("id");
      if (idObj instanceof String) {
        String id = (String) idObj;
        log.info("Naver ID: {}", id);
        return id;
      }
    }
    return null;
  }
}