package com.momo.momopjt.user;


import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.security.Principal;
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
        String errorCode = request.getParameter("errorCode");
        String logout = request.getParameter("logout");

        log.info("login get........");
        log.info("logout: " + logout);

        if (logout != null) {

            log.info("user logout......");
        }
    }

    @GetMapping("/join")
    public void joinGet() {
        log.info("Processing GET request for /join");
    }

    @PostMapping("/join")
    public String joinPost(@Valid UserJoinDTO userJoinDTO, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        log.info("Processing POST request for /join with data: {}", userJoinDTO);

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors());
            return "redirect:/user/join";
        }
        try {
            userService.join(userJoinDTO);
        } catch (UserService.UserIdException e) {
            redirectAttributes.addFlashAttribute("error", "userId");
            return "redirect:/user/join";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "An unexpected error occurred.");
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


    @GetMapping("/editProfile")
    public String editProfileForm(Model model, @AuthenticationPrincipal User user) {
        if (user == null) {
            // 사용자 객체가 null일 경우 처리
            model.addAttribute("errorMessage", "사용자 정보를 가져오는 데 실패했습니다.");
            return "errorPage";  // 에러 페이지로 이동
        }

        UserJoinDTO userJoinDTO = new UserJoinDTO();

        if (user.getUserNickname() != null) {
            userJoinDTO.setUserNickname(user.getUserNickname());
        }
        if (user.getUserEmail() != null) {
            userJoinDTO.setUserEmail(user.getUserEmail());
        }
        if (user.getUserCategory() != null) {
            userJoinDTO.setUserCategory(user.getUserCategory());
        }
        if (user.getUserAddress() != null) {
            userJoinDTO.setUserAddress(user.getUserAddress());
        }
        if (user.getUserMbti() != null) {
            userJoinDTO.setUserMbti(user.getUserMbti());
        }

        model.addAttribute("user", userJoinDTO);
        return "home";  // 뷰 이름을 "home"으로 변경
    }

    @PostMapping("/editProfile")
    public String editProfile(@Valid @ModelAttribute("user") UserJoinDTO userJoinDTO, BindingResult bindingResult, RedirectAttributes redirectAttributes, @AuthenticationPrincipal User user) {
        if (bindingResult.hasErrors()) {
            return "user/editProfile";  // 에러가 있을 경우 동일한 뷰로 돌아감
        }

        if (user == null) {
            // 사용자 객체가 null일 경우 처리
            redirectAttributes.addFlashAttribute("errorMessage", "사용자 정보를 가져오는 데 실패했습니다.");
            return "redirect:/login";  // 로그인 페이지로 리디렉션
        }

        try {
            userService.updateUser(userJoinDTO, user);
            redirectAttributes.addFlashAttribute("successMessage", "회원정보가 성공적으로 수정되었습니다.");
            return "redirect:/user/home";  // 성공 시 리디렉션 경로 수정
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "회원정보 수정 중 오류가 발생했습니다.");
            return "redirect:/user/editProfile";  // 에러 발생 시 동일한 경로로 리디렉션
        }
    }
}