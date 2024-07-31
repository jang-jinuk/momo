package com.momo.momopjt.global.security.handler;


import com.momo.momopjt.user.User;
import com.momo.momopjt.user.UserRepository;
import com.momo.momopjt.user.UserRole;
import com.momo.momopjt.user.UserSecurityDTO;
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

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.Instant;
import java.util.Map;

@Log4j2
@RequiredArgsConstructor
public class CustomSocialLoginSuccessHandler implements AuthenticationSuccessHandler {

  private final PasswordEncoder passwordEncoder;
  private final UserRepository userRepository;

  @Override
  public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                      Authentication authentication) throws IOException, ServletException {
    log.info("---------------------------------------");
    log.info("CustomSocialLoginSuccessHandler onAuthenticationSuccess............");
    log.info(authentication.getPrincipal());

    Object principal = authentication.getPrincipal();
    String id;
    String email;
    Character social;

    if (principal instanceof DefaultOAuth2User) {
      DefaultOAuth2User oAuth2User = (DefaultOAuth2User) principal;
      String provider = ((OAuth2AuthenticationToken) authentication).getAuthorizedClientRegistrationId();

      // Extract user info based on provider
      if ("google".equals(provider)) {
        id = (String) oAuth2User.getAttribute("sub");
        email = (String) oAuth2User.getAttribute("email");
        social = 'G';
      } else if ("kakao".equals(provider)) {
        Object idObject = oAuth2User.getAttribute("id");
        id = idObject instanceof String ? (String) idObject : Long.toString((Long) idObject);
        email = getKakaoEmail(oAuth2User.getAttributes());
        social = 'K';
      } else if ("naver".equals(provider)) {
        id = getNaverId(oAuth2User.getAttributes());
        email = getNaverEmail(oAuth2User.getAttributes());
        social = 'N';
      } else {
        throw new OAuth2AuthenticationException("Unsupported provider: " + provider);
      }

      if (id == null || email == null) {
        throw new OAuth2AuthenticationException("ID or email not found from provider: " + provider);
      }

      User user = userRepository.findByUserEmail(email).orElse(null);
      if (user == null) {
        // New user: Create and save user, then redirect to update page
        user = User.builder()
            .userId(id)
            .userEmail(email)
            .userPw(passwordEncoder.encode("defaultPassword")) // Default password
            .userSocial(social)
            .userRole(UserRole.USER)
            .userCreateDate(Instant.now())
            .userState('0')
            .build();
        userRepository.save(user);
        log.info("New user saved to database: {}", user);

        // Set new user in SecurityContext
        UserSecurityDTO userSecurityDTO = new UserSecurityDTO(id, passwordEncoder.encode("defaultPassword"), email, true, social, oAuth2User.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(
            new UsernamePasswordAuthenticationToken(userSecurityDTO, null, oAuth2User.getAuthorities())
        );

        response.sendRedirect(request.getContextPath() + "/user/update/" + id);
        return;
      }

      // Existing user: Update security context and redirect to home
      UserSecurityDTO userSecurityDTO = new UserSecurityDTO(user.getUserId(), user.getUserPw(), user.getUserEmail(), true, user.getUserSocial(), oAuth2User.getAuthorities());
      SecurityContextHolder.getContext().setAuthentication(
          new UsernamePasswordAuthenticationToken(userSecurityDTO, null, oAuth2User.getAuthorities())
      );
      log.info("UserSecurityDTO created and set in SecurityContext: {}", userSecurityDTO);
    } else if (principal instanceof UserSecurityDTO) {
      // For existing non-OAuth2 users
      UserSecurityDTO userSecurityDTO = (UserSecurityDTO) principal;
      log.info("Existing user logged in: {}", userSecurityDTO);
    }

    // Redirect to home for all users
    response.sendRedirect(request.getContextPath() + "/home");
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