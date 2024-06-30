package com.momo.momopjt.user;


import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.time.Period;

@Controller
@RequestMapping("/user")
@RequiredArgsConstructor
@Log4j2
public class UserController {
    @GetMapping("/login")
    public String login() {

        return "login";
    }

    @GetMapping("/join")
    public void joinGet() {
        log.info("join get...");
    }

    @PostMapping("/join")
    public String joinPost(User user) {
        log.info("join post...");
        log.info(user);

        return "redirect:/login";
        //main 생길시 넣을거임
    }


    @PostMapping("/submit")
    public String handleFormSubmit(
            @RequestParam("userId") String userId,
            @RequestParam("userNickname") String userNickname,
            @RequestParam("userPw") String userPw, // 비밀번호도 받지만 결과 페이지에 표시하지 않음
            @RequestParam("userGender") char userGender,
            @RequestParam("userBirth") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate userBirth,
            @RequestParam("userEmail") String userEmail,
            @RequestParam("userCategory") String userCategory,
            @RequestParam("userAddress") String userAddress,
            @RequestParam("userMbti") String userMbti,
            Model model) {

        // 현재 날짜를 기준으로 나이 계산
        LocalDate currentDate = LocalDate.now();
        int userAge = Period.between(userBirth, currentDate).getYears();

        // 비밀번호는 추가하지 않음
        model.addAttribute("userId", userId);
        model.addAttribute("userNickname", userNickname);
        model.addAttribute("userGender", userGender);
        model.addAttribute("userBirth", userBirth);
        model.addAttribute("userEmail", userEmail);
        model.addAttribute("userCategory", userCategory);
        model.addAttribute("userAge", userAge);
        model.addAttribute("userAddress", userAddress);
        model.addAttribute("userMbti", userMbti);

        return "result"; // 결과를 표시할 뷰의 이름
    }


}
