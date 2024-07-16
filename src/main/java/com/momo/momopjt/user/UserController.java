package com.momo.momopjt.user;



import com.momo.momopjt.user.find.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
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
@Transactional
public class UserController {

    private final UserService userService;
    private final UserRepository userRepository;
    private final EmailService emailService;
private final ModelMapper modelMapper;

    @GetMapping("/home")
    public String home(@AuthenticationPrincipal UserDTO userDTO, Model model) {
        if (userDTO != null) {
            model.addAttribute("nickname", userDTO.getUserNickname());
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

    @GetMapping("/signup")
    public void signupGet() {
        log.info("Processing GET request for /signup");
    }

    @PostMapping("/signup")
    public String signupPost(@Valid UserDTO userDTO, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        log.info("Processing POST request for /signup with data: {}", userDTO);

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors());
            return "redirect:/user/signup";
        }
        try {
            userService.signup(userDTO);
        } catch (UserService.UserIdException e) {
            redirectAttributes.addFlashAttribute("errorId", "userId");
            return "redirect:/user/signup";

        } catch (UserService.UserEmailException e) {
            redirectAttributes.addFlashAttribute("errorEmail", "userEmail");
            return "redirect:/user/signup";

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorException", "An unexpected error occurred.");
            return "redirect:/user/signup";
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
        User user = userRepository.findByUserId(userId);
        if (user == null) {
            throw new IllegalArgumentException("User not found with userId: " + userId);
        }

        UserDTO userDTO = modelMapper.map(user, UserDTO.class);
//        userDTO.setUserId(user.getUserId());
//        userDTO.setUserEmail(user.getUserEmail());
//        userDTO.setUserNickname(user.getUserNickname());
//        userDTO.setUserCategory(user.getUserCategory());
//        userDTO.setUserAddress(user.getUserAddress());
//        userDTO.setUserMBTI(user.getUserMBTI());

        model.addAttribute("userDTO", userDTO);
        return "user/update";
    }

    @PostMapping("/update")
    public String updatePost(@ModelAttribute("userDTO") @Valid UserDTO userDTO,
                             BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        log.info("Processing POST request for /update with data: {}", userDTO);
        String userId = userDTO.getUserId();

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors());
            log.error(bindingResult.getAllErrors().toString());
            return "redirect:/user/update/" + userId;
        }
        try {
            userService.updateUser(userDTO);
        } catch (Exception e) {
            log.error("Failed to update user with userId: {}", userId, e);
            redirectAttributes.addFlashAttribute("error", "Failed to update user.");
            return "redirect:/user/update/" + userId;
        }

        redirectAttributes.addFlashAttribute("result", "success");
        return "redirect:/user/home";
    }
}

