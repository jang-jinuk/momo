package com.momo.momopjt.user;


import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

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

}