package com.momo.momopjt.global.config;

import com.momo.momopjt.global.security.CustomOAuth2UserService;
import com.momo.momopjt.global.security.CustomUserDetailService;
import com.momo.momopjt.global.security.handler.CustomSocialLoginSuccessHandler;
import com.momo.momopjt.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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
        .csrf(csrf -> csrf.disable())
        .authorizeHttpRequests(auth -> auth
                .requestMatchers("/secured/**").authenticated()
                .requestMatchers("/", "/register", "/login", "/css/**", "/js/**",
                    "/images/**", "/public/**", "/user/**", "/find/**","/article/**","/assets/**").permitAll()
                .requestMatchers("/admin/**").hasRole("ADMIN")
            .anyRequest().authenticated()

        )
        .formLogin(form -> form
            .loginPage("/user/login")
            .defaultSuccessUrl("/")
            .successHandler(authenticationSuccessHandler())
            .permitAll()
        )
        .logout(logout -> logout
            .logoutRequestMatcher(new AntPathRequestMatcher("/user/logout"))
            .logoutSuccessUrl("/")
            .invalidateHttpSession(true)
            .deleteCookies("JSESSIONID")
            .permitAll()
        )
        .exceptionHandling(exception -> exception
            .accessDeniedPage("/403")
        )
        .sessionManagement(session -> session
            .invalidSessionUrl("/user/login?expired=true")
            .maximumSessions(1)
            .expiredUrl("/user/login?expired=true")
        );

    // 소셜 로그인 설정
    http.oauth2Login(oauth2 -> oauth2
        .loginPage("/user/login")
        .defaultSuccessUrl("/", true)
        .successHandler(authenticationSuccessHandler())
        .userInfoEndpoint(userInfo -> userInfo
            .userService(customOAuth2UserService)
        )
    );

    return http.build();
  }
}