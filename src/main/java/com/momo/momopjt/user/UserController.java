package com.momo.momopjt.user;


import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.time.LocalDate;
import java.time.Period;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Controller
@RequestMapping("/user")
@RequiredArgsConstructor
@Log4j2
public class UserController {

    private final UserService userService;

    @GetMapping("/home")
    public String home(@AuthenticationPrincipal UserDto userDto, Model model) {
        if (userDto != null) {
            model.addAttribute("nickname", ((UserDto) userDto).getUserNickname());
        }
        return "home"; // Thymeleaf 템플릿 이름
    }

    @GetMapping("/login")
    public void loginGET(HttpServletRequest request) {
        log.info("-------- [07-03-13:16:04]-------you");
        String errorCode = request.getParameter("errorCode");
        log.info("-------- [07-03-13:16:11]-------you");
        String logout = request.getParameter("logout");

        log.info("login get........");
        log.info("logout: " + logout);

        if (logout != null) {
            log.info("-------- [07-03-13:16:16]-------you");
            log.info("user logout......");
        }
        log.info("-------- [07-03-13:16:20]-------you");
    }

    @GetMapping("/join")
    public void joinGet() {
        log.info("join get...");
    }

    @PostMapping("/join")
    public String joinPost(@Valid UserJoinDTO userJoinDTO, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        log.info("join post...");
        log.info(userJoinDTO);

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors());
            return "redirect:/user/join";
        }

        try {
            userService.join(userJoinDTO);
        } catch (UserService.UserIdException e) {
            redirectAttributes.addFlashAttribute("error", "userId");
            return "redirect:/user/join";
        }

        redirectAttributes.addFlashAttribute("result", "success");
        return "redirect:/user/home"; // 회원가입 후 홈으로
    }

    @PostMapping("/logout")
    public String logout(HttpServletRequest request, RedirectAttributes redirectAttributes) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
            log.info("User logged out...");
        }

        redirectAttributes.addFlashAttribute("message", "You have been logged out successfully.");
        return "redirect:/user/login"; // 로그아웃 후 로그인 페이지로 리다이렉트
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