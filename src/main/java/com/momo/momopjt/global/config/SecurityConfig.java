package com.momo.momopjt.global.config;

import com.momo.momopjt.global.security.CustomUserDetailService;
import com.momo.momopjt.global.security.handler.CustomSocialLoginSuccessHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;


@Log4j2
@Configuration
@RequiredArgsConstructor
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig {


  private final CustomUserDetailService customUserDetailService;

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean   //OAuth2로그인 관련해서 CustomSocialLoginSuccessHandler를 로그인 성공 처리시 이용하는 부분
  public AuthenticationSuccessHandler authenticationSuccessHandler(){
    return new CustomSocialLoginSuccessHandler(passwordEncoder());
  }

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    log.info("------------configure----------------");

    http
        .authorizeRequests()
        .antMatchers("/", "/home", "/register", "/login", "/css/**", "/js/**", "/images/**",
            "/public/**", "/user/login", "/user/join", "/user/home", "/user/update", "/user/profile", "/user/**").permitAll()
        .antMatchers("/admin/**").hasRole("ADMIN") // 관리자 페이지 접근 제한
        .anyRequest().authenticated() // 다른 모든 요청은 인증 필요
        .and()
        .formLogin()
        .loginPage("/user/login") // 로그인 페이지 설정
        .defaultSuccessUrl("/user/home") // 로그인 성공 후 이동할 페이지
        .permitAll()
        .and()
        .logout()
        .logoutRequestMatcher(new AntPathRequestMatcher("/user/logout")) // 로그아웃 경로 설정
        .logoutSuccessUrl("/user/home")
        .invalidateHttpSession(true)          // 로그아웃 시 세션을 무효화
        .deleteCookies("JSESSIONID")   // 로그아웃 시 특정 쿠키(JSESSIONID)를 삭제
        .permitAll()
        .and()
        .exceptionHandling()
        .accessDeniedPage("/403") // 접근 거부 시 이동할 페이지 설정
        .and()
        .csrf().disable() // CSRF 비활성화
        .oauth2Login()
        .loginPage("/user/login") // 카카오 로그인 페이지 설정
        .successHandler(authenticationSuccessHandler());

    return http.build();
  }

  @Bean
  public WebSecurityCustomizer webSecurityCustomizer() {
    log.info("------------web configure----------");

    // 정적 리소스 무시
    return (web) -> web.ignoring()
        .antMatchers("/resources/**", "/static/**", "/css/**", "/js/**", "/images/**");
  }

//  @Bean
//  public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
//    AuthenticationManagerBuilder auth = http.getSharedObject(AuthenticationManagerBuilder.class);
//    auth.userDetailsService(customUserDetailService).passwordEncoder(passwordEncoder());
//    return auth.build();
//  }
//  /*
//  이 코드는 Spring Security의 AuthenticationManager를 설정하는 방법을 보여줍니다.
//  사용자 정의 UserDetailsService와 비밀번호 인코더를 설정하여,
//  사용자 인증을 처리하는 AuthenticationManager를 생성하고 이를 Spring 컨텍스트에 빈으로 등록합니다.
//  이를 통해 애플리케이션 내에서 해당 AuthenticationManager를 주입받아 사용
//   */


}