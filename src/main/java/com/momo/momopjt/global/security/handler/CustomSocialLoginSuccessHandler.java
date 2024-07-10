package com.momo.momopjt.global.security.handler;


import com.momo.momopjt.user.User;
import com.momo.momopjt.user.UserRepository;
import com.momo.momopjt.user.UserSecurityDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.security.core.GrantedAuthority;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;
import org.springframework.transaction.annotation.Transactional;

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
      String id = (String) oAuth2User.getAttribute("sub");
      String email = (String) oAuth2User.getAttribute("email");
      String password = passwordEncoder.encode("defaultPassword"); // 적절한 기본 비밀번호 설정
      boolean enabled = true; // 계정 활성화 여부
      Character social = 'G'; // 소셜 로그인 여부 (예: Google)
      Collection<? extends GrantedAuthority> authorities = oAuth2User.getAuthorities();
      userSecurityDTO = new UserSecurityDTO(id, password, email, enabled, social, authorities);
      encodedPw = userSecurityDTO.getUserPw();

      // 사용자 엔티티 생성 및 저장
      User user = userRepository.findByUserId(id);
      if (user == null) {
        user = new User();
        user.setUserId(id);
        user.setUserEmail(email);
        user.setUserPw(encodedPw);
        user.setUserSocial('G');
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
}