package com.momo.momopjt.global.config;

import com.momo.momopjt.global.security.CustomOAuth2UserService;
import com.momo.momopjt.global.security.CustomUserDetailService;
import com.momo.momopjt.global.security.handler.CustomSocialLoginSuccessHandler;
import com.momo.momopjt.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;


@Log4j2
@Configuration
@RequiredArgsConstructor
@EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableWebSecurity
public class SecurityConfig {

  private final CustomUserDetailService customUserDetailService;
  private final CustomOAuth2UserService customOAuth2UserService;
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
        .csrf().disable()// CSRF 보호 활성화
        .authorizeRequests()
        .antMatchers("/secured/**").authenticated()
        .antMatchers("/find/**").permitAll()
        .antMatchers("/", "/home", "/register", "/login", "/css/**", "/js/**", "/images/**", "/public/**", "/user/**", "/find/**","/article/**").permitAll()
        .antMatchers("/admin/**").hasRole("ADMIN")
        .and()
        .formLogin().loginPage("/user/login")
        .defaultSuccessUrl("/home")
        .successHandler(authenticationSuccessHandler())
        .permitAll()
        .and()
        .logout()
        .logoutRequestMatcher(new AntPathRequestMatcher("/user/logout"))
        .logoutSuccessUrl("/home")
        .invalidateHttpSession(true)
        .deleteCookies("JSESSIONID")
        .permitAll()
        .and()
        .exceptionHandling()
        .accessDeniedPage("/403")
        .and()
        .sessionManagement()  // 세션 관리 설정 추가
        .invalidSessionUrl("/user/login?expired=true")  // 세션이 무효화되었을 때 리다이렉트할 URL 추가
        .maximumSessions(1)  // 동시 세션 최대 수 설정
        .expiredUrl("/user/login?expired=true");  // 세션 만료 시 리다이렉트할 URL 추가


    // 소셜 로그인 설정
    http
        .oauth2Login()
        .loginPage("/user/login")
        .defaultSuccessUrl("/home", true)
        .successHandler(authenticationSuccessHandler())
        .userInfoEndpoint()
        .userService(customOAuth2UserService);

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

//  @Bean
//  public OAuth2UserService<OAuth2UserRequest, OAuth2User> customOAuth2UserService2() {
//    return new CustomOAuth2UserService(userRepository);
//  }
}