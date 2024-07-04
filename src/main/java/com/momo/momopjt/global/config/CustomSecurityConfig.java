package com.momo.momopjt.global.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@Log4j2
@EnableWebSecurity
@RequiredArgsConstructor
public class CustomSecurityConfig {


  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http,
           AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {

    log.info("----------------- SecFilterChain [05-31 16:04:55]-----------------");

    return http

          .csrf(AbstractHttpConfigurer::disable)
          .authorizeHttpRequests(req ->
                req
                  .requestMatchers().permitAll()
                  .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
                  .anyRequest().permitAll()
          )
          .sessionManagement(session ->
              session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
          )
          .build();


  }
}




