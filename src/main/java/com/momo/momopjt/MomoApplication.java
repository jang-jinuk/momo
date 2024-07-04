package com.momo.momopjt;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;

@SpringBootApplication

public class MomoApplication {

    public static void main(String[] args) {
        SpringApplication.run(MomoApplication.class, args);
    }
}