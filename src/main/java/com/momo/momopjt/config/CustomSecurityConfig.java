package com.momo.momopjt.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Log4j2
@Configuration
@RequiredArgsConstructor
@EnableGlobalMethodSecurity(prePostEnabled = true)
/*
prePostEnabld 속성은 원하는 곳에 @PreAutho-rize 혹은 @PostAuthorize 어노테이션을 이용해서
사전 혹은 사후의 권한을 체크할 수 있다.
 */
public class CustomSecurityConfig {
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{

        log.info("------------configure----------------");

        // 영진오빠가 작성해준 코드 http.formLogin().disable();
        http.formLogin().loginPage("/club/register");


        return http.build();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        log.info("------------web configure----------");

        return (web) -> web.ignoring().requestMatchers(PathRequest.
                toStaticResources().atCommonLocations());

    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}

