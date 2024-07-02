package com.momo.momopjt.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@PreAuthorize("hasRole('USER')")

@Controller
@RequestMapping("/club")
@Log4j2
@RequiredArgsConstructor
public class ClubController {

    @GetMapping("/login")
    public void loginGet(String error, String logout){
        log.info("login get...........");
        log.info("logout:" +logout);
    }

//  모임 생성 페이지
    @GetMapping("/register")
    public void registerGET(){

    }

//  모임 생성 기능
    @PostMapping("/register")
    public String registerPOST(){


        return "register";
    }
}
