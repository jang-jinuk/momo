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
    private final UserRepository userRepository;


    @GetMapping("/home")
    public String home(@AuthenticationPrincipal UserDTO userDto, Model model) {
        if (userDto != null) {
            model.addAttribute("nickname", ((UserDTO) userDto).getUserNickname());
        }
        return "/home"; // Thymeleaf 템플릿 이름
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


    @GetMapping("/update/{userId}")
    public String updateGet(@PathVariable String userId, Model model) {
        UserUpdateDTO userUpdateDTO = new UserUpdateDTO(); // 빈 객체 생성 또는 초기화
        // 사용자 정보를 조회하여 폼에 바인딩
        // 예시로 userRepository를 사용하여 사용자 정보를 조회하는 경우
        User user = userRepository.findByUserId(userId);
        if (user == null) {
            throw new IllegalArgumentException("User not found with userId: " + userId);
        }
        userUpdateDTO.setUserId(user.getUserId());
//        userUpdateDTO.setUserPw(user.getUserPw());
        userUpdateDTO.setUserEmail(user.getUserEmail());
        userUpdateDTO.setUserNickname(user.getUserNickname());
        userUpdateDTO.setUserCategory(user.getUserCategory());
        userUpdateDTO.setUserAddress(user.getUserAddress());
        userUpdateDTO.setUserMBTI(user.getUserMBTI());

        model.addAttribute("userUpdateDTO", userUpdateDTO);
        return "user/update";
    }

    @PostMapping("/update")
    public String updatePost(@Valid UserUpdateDTO userUpdateDTO,
                             BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        log.info("Processing POST request for /update with data: {}", userUpdateDTO);
        String userId = userUpdateDTO.getUserId();
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors());
            return "redirect:/user/update/" + userId;
        }
        try {
            userService.updateUser(userUpdateDTO);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to update user.");
            return "redirect:/user/update/" + userId;
        }

        redirectAttributes.addFlashAttribute("result", "success");
        return "redirect:/user/home";
    }
}