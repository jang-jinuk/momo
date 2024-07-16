package com.momo.momopjt.global.config;

import com.momo.momopjt.global.security.CustomUserDetailService;
import com.momo.momopjt.global.security.NaverOAuth2UserInfo;
import com.momo.momopjt.global.security.handler.CustomSocialLoginSuccessHandler;
import com.momo.momopjt.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.util.Collections;
import java.util.Map;


@Log4j2
@Configuration
@RequiredArgsConstructor
@EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableWebSecurity
public class SecurityConfig {

  private final CustomUserDetailService customUserDetailService;
  private final UserRepository userRepository;

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public AuthenticationSuccessHandler authenticationSuccessHandler() {
    return new CustomSocialLoginSuccessHandler(passwordEncoder(), userRepository);
  }

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    log.info("------------configure----------------");

    http
        .authorizeRequests()
        .antMatchers("/secured/**").authenticated()
        .antMatchers("/find/**").permitAll()
        .antMatchers("/", "/home", "/register", "/login", "/css/**", "/js/**", "/images/**", "/public/**", "/user/**", "/find/**").permitAll()
        .antMatchers("/admin/**").hasRole("ADMIN")
        .and()
        .formLogin().loginPage("/user/login")
        .defaultSuccessUrl("/user/home")
        .successHandler(authenticationSuccessHandler())
        .permitAll()
        .and()
        .logout()
        .logoutRequestMatcher(new AntPathRequestMatcher("/user/logout"))
        .logoutSuccessUrl("/user/home")
        .invalidateHttpSession(true)
        .deleteCookies("JSESSIONID")
        .permitAll()
        .and()
        .exceptionHandling()
        .accessDeniedPage("/403")
        .and()
        .csrf().disable();

    http.oauth2Login()
        .loginPage("/user/login")
        .defaultSuccessUrl("/user/home", true)
        .successHandler(authenticationSuccessHandler())
        .userInfoEndpoint()
        .userService(oAuth2UserService());

    return http.build();
  }


  //0716 불필요 코드 주석처리 YY
//  @Bean
//  public WebSecurityCustomizer webSecurityCustomizer() {
//    log.info("------------web configure----------");
//
//    return (web) -> web.ignoring()
//        .antMatchers("/resources/**", "/static/**", "/css/**", "/js/**", "/images/**");
//  }

  @Bean
  public OAuth2UserService<OAuth2UserRequest, OAuth2User> oAuth2UserService() {
    DefaultOAuth2UserService delegate = new DefaultOAuth2UserService();

    return request -> {
      OAuth2User oAuth2User = delegate.loadUser(request);

      String registrationId = request.getClientRegistration().getRegistrationId();
      String userNameAttributeName = request.getClientRegistration().getProviderDetails()
          .getUserInfoEndpoint().getUserNameAttributeName();

      Map<String, Object> attributes = oAuth2User.getAttributes();
      Map<String, Object> additionalParameters = request.getAdditionalParameters();

      // 사용자 정보를 파싱하는 로직을 추가합니다.
      NaverOAuth2UserInfo userInfo = new NaverOAuth2UserInfo(attributes);

      // 필요한 사용자 정보를 추출합니다.
      String userId = userInfo.getId();
      String userName = userInfo.getName();
      String userEmail = userInfo.getEmail();

      // 사용자 정보를 이용하여 데이터베이스에 사용자 정보를 저장하거나 갱신하는 로직을 추가할 수 있습니다.
      return new DefaultOAuth2User(
          Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")),
          attributes,
          userNameAttributeName
      );
    };
  }
}