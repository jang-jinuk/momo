package com.momo.momopjt.user;


import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;

@Controller
@RequestMapping
@RequiredArgsConstructor
@Log4j2
public class UserController {
    @GetMapping("/login")
    public String login() {

        return "login";
    }

    @GetMapping("/user/join")
    public void joinGet() {
        log.info("join get...");
    }

    @PostMapping("/user/join")
    public String joinPost(User user) {
        log.info("join post...");
        log.info(user);

        return "redirect:/login";
        //main 생길시 넣을거임
    }

    /*@PostMapping("/submit")
    public String handleFormSubmit(@RequestParam("userBirth") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate userBirth) {// birthDate를 사용한 로직 처리
        return "result";
    }

    @PostMapping("/submit")
    public String handleFormSubmit(@RequestParam("email") String userEmail) {
        return "result";
    }


     */
}
