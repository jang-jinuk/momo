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
        .antMatchers("/secured/**").authenticated()
        .antMatchers("/find/**").permitAll()
        .antMatchers("/", "/home", "/register", "/login", "/css/**", "/js/**", "/images/**",
            "/public/**", "/user/**","/find/**").permitAll()
        .antMatchers("/admin/**").hasRole("ADMIN")
//        .anyRequest().authenticated()
        .and()
        .formLogin()
        .loginPage("/user/login")
        .defaultSuccessUrl("/home")
        .successHandler(authenticationSuccessHandler()) // 사용자 정의 핸들러 추가
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
        .oauth2Login() // OAuth2 로그인 설정
        .loginPage("/user/login")
        .and()
        .csrf().disable();

    return http.build();
  }



  @Bean
  public WebSecurityCustomizer webSecurityCustomizer() {
    log.info("------------web configure----------");

    // 정적 리소스 무시
    return (web) -> web.ignoring()
        .antMatchers("/resources/**", "/static/**", "/css/**", "/js/**", "/images/**");
  }
}