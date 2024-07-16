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

@Service
@Log4j2
@RequiredArgsConstructor
@Component
public class CustomSocialLoginSuccessHandler implements AuthenticationSuccessHandler {

  private final PasswordEncoder passwordEncoder;
  private final UserRepository userRepository;

  @Override
  @Transactional
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


      //TODO IF-Else 문을 Switch - Case 문으로 수정 하면 될듯. 0716 YY
      if ("google".equals(provider)) {
        id = (String) oAuth2User.getAttribute("sub");
        email = (String) oAuth2User.getAttribute("email");
        social = 'G';
      } else if ("kakao".equals(provider)) {
        Object idObject = oAuth2User.getAttribute("id");
        if (idObject instanceof String) {
          id = (String) idObject;
        } else if (idObject instanceof Long) {
          id = Long.toString((Long) idObject); // Long 타입인 경우 문자열로 변환
        } else {
          throw new IllegalStateException("Unexpected type for 'id' attribute: " + idObject.getClass().getName());
        }
        email = getKakaoEmail(oAuth2User.getAttributes());
        social = 'K';
      } else if ("naver".equals(provider)) {
        id = getNaverId(oAuth2User.getAttributes());
        email = getNaverEmail(oAuth2User.getAttributes());
        social = 'N';
      }

      if (id == null || email == null) {
        throw new OAuth2AuthenticationException("ID or email not found from provider: " + provider);
      }

      String password = passwordEncoder.encode("defaultPassword"); // 적절한 기본 비밀번호 설정
      boolean enabled = true; // 계정 활성화 여부
      Collection<? extends GrantedAuthority> authorities = oAuth2User.getAuthorities();
      userSecurityDTO = new UserSecurityDTO(id, password, email, enabled, social, authorities);
      encodedPw = userSecurityDTO.getUserPw();

      // 사용자 엔티티 생성 및 저장
      User user = userRepository.findByUserId(id);
      if (user == null) {
        user = User.builder()
            .userId(id)
            .userEmail(email)
            .userPw(encodedPw)
            .userSocial(social)
            .userRole(UserRole.USER) // 기본값 설정
            .userCreateDate(Instant.now()) // 생성 날짜 설정
            .userState('0')
            .build();
        userRepository.save(user);
        log.info("New user saved to database: {}", user);
      }

      SecurityContextHolder.getContext().setAuthentication(
          new UsernamePasswordAuthenticationToken(userSecurityDTO, null, oAuth2User.getAuthorities())
      );
      log.info("UserSecurityDTO created and set in SecurityContext: {}", userSecurityDTO);
    } else if (principal instanceof UserSecurityDTO) {
      userSecurityDTO = (UserSecurityDTO) principal;
      encodedPw = userSecurityDTO.getUserPw();
    }

    // 비밀번호 변경 로직
    if (userSecurityDTO != null && userSecurityDTO.getUserSocial() == 'Y' && passwordEncoder.matches("1111", encodedPw)) {
      log.info("Should Change Password");
      log.info("Redirect to User update");
      response.sendRedirect(request.getContextPath() + "/user/update");
    } else {
      // 일반 로그인 흐름
      response.sendRedirect(request.getContextPath() + "/user/home");
    }
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