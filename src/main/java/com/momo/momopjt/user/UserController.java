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
            model.addAttribute("nickname", userDto.getUserNickname());
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
    public String joinPost(@Valid UserDTO userDTO, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        log.info("Processing POST request for /join with data: {}", userDTO);

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors());
            return "redirect:/user/join";
        }
        try {
            userService.join(userDTO);
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
        User user = userRepository.findByUserId(userId);
        if (user == null) {
            throw new IllegalArgumentException("User not found with userId: " + userId);
        }

        UserDTO userDTO = new UserDTO();
        userDTO.setUserId(user.getUserId());
        userDTO.setUserEmail(user.getUserEmail());
        userDTO.setUserNickname(user.getUserNickname());
        userDTO.setUserCategory(user.getUserCategory());
        userDTO.setUserAddress(user.getUserAddress());
        userDTO.setUserMBTI(user.getUserMBTI());

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


    // 유정작업중 -----------------------------------


    @GetMapping("/find")
    public String showFindForm(Model model) {
        model.addAttribute("findUserIdRequest", new FindUserIdRequest());
        model.addAttribute("findPasswordRequest", new FindPasswordRequest());
        return "user/find/idPw";
    }

    // 아이디 찾기 처리
    @PostMapping("/find/userid")
    public String findUserId(@ModelAttribute("findUserIdRequest") FindUserIdRequest findUserIdRequest, Model model) {
        String userEmail = findUserIdRequest.getFindUserEmail();
        String userId = userService.findUsernameByEmail(userEmail); // 메서드명도 findUserIdByEmail로 변경

        if (userId != null) {
            model.addAttribute("userId", userId); // 변수명을 userId로 변경
        } else {
            model.addAttribute("errorMessageUserId", "User ID not found for email: " + userEmail);
        }

        return "user/find/idPw";
    }
    // 비밀번호 찾기 폼 렌더링
    @GetMapping("/findPassword")
    public String showFindPasswordForm(Model model) {
        model.addAttribute("findPasswordRequest", new FindPasswordRequest());
        return "user/find/idPw";
    }

    // 비밀번호 찾기 처리
    @PostMapping("/find/password")
    public String findPassword(@ModelAttribute("findPasswordRequest") FindPasswordRequest findPasswordRequest,
                               RedirectAttributes redirectAttributes) {
        String userId = findPasswordRequest.getUserId();
        String userEmail = findPasswordRequest.getEmail();
        User user = userService. findByUserIdAndUserEmail(userId, userEmail);

        if (user != null) {
            // 사용자가 존재하면 비밀번호 재설정 링크를 전송하는 로직을 추가할 수 있음
            // 이메일 전송 등의 코드를 여기에 추가할 수 있음
            redirectAttributes.addFlashAttribute("resetPasswordMessage", "Instructions to reset your password have been sent to your email.");
            return "redirect:/findPassword";
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "User not found for username: " + userId + " and email: " + userEmail);
            return "redirect:/findPassword";
        }
    }

    // 비밀번호 재설정 폼 렌더링
    @GetMapping("/reset")
    public String showResetPasswordForm(Model model) {
        model.addAttribute("resetPasswordRequest", new ResetPasswordRequest());
        return "user/find/resetPassword"; // resetPassword.html에 해당하는 템플릿 이름
    }

    // 비밀번호 재설정 처리
    @PostMapping("/reset/userPw")
    public String resetPassword(@ModelAttribute("resetPasswordRequest") ResetPasswordRequest resetPasswordRequest,
                                RedirectAttributes redirectAttributes,
                                Model model) {
        String userId = resetPasswordRequest.getResetPasswordUserId();
        String userEmail = resetPasswordRequest.getResetPasswordEmail();
        String newPassword = resetPasswordRequest.getResetPassword();

        // 비밀번호 일치 검사
        if (!newPassword.equals(resetPasswordRequest.getResetPasswordConfirm())) {
            model.addAttribute("passwordMismatchError", "Passwords do not match.");
            model.addAttribute("resetPasswordRequest", resetPasswordRequest); // 모델에 다시 추가
            return "user/find/resetPassword";
        }

        // userService의 resetPassword() 메서드 호출
        boolean resetSuccess = userService.resetPassword(userId, userEmail, newPassword);

        if (resetSuccess) {
            redirectAttributes.addFlashAttribute("resetSuccessMessage", "Password reset successful!");
            return "redirect:/user/find/passwordResetSuccess"; // 비밀번호 재설정 성공 메시지 페이지로 리다이렉트
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to reset password. Please try again.");
            return "redirect:/reset";
        }
    }

    // 비밀번호 재설정 성공 메시지 페이지 렌더링
    @GetMapping("/find/passwordResetSuccess")
    public String showPasswordResetSuccess() {
        return "user/find/passwordResetSuccess"; // passwordResetSuccess.html에 해당하는 템플릿 이름
    }
}