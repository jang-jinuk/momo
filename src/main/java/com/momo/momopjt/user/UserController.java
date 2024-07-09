package com.momo.momopjt.user;



import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private final EmailService emailService;
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

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
            redirectAttributes.addFlashAttribute("errorId", "userId");
            return "redirect:/user/join";

        } catch (UserService.UserEmailException e) {
            redirectAttributes.addFlashAttribute("errorEmail", "userEmail");
            return "redirect:/user/join";

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorException", "An unexpected error occurred.");
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


    @GetMapping("/find-id")
    public String showFindIdForm(Model model) {
        model.addAttribute("findUserIdRequest", new FindUserIdRequest());
        return "user/find/id";
    }

    // 아이디 찾기
    @PostMapping("/userid")
    public String findUserId(@ModelAttribute("findUserIdRequest") FindUserIdRequest findUserIdRequest, Model model) {
        String userEmail = findUserIdRequest.getFindUserEmail();
        String userId = userService.findUsernameByEmail(userEmail);
        if (userId != null) {
            model.addAttribute("userId", userId);
            logger.info("Found userId: {}", userId); // 유저 아이디 찾음을 로그
        } else {
            model.addAttribute("errorMessageUserId", "User ID not found for email: " + userEmail);
            logger.warn("User ID not found for email: {}", userEmail); // 아이디를 못 찾음을 경고 로그
        }
        return "user/find/id";
    }

    // 비밀번호 찾기 폼 렌더링
    @GetMapping("/find-pw")
    public String showFindPasswordForm(Model model) {
        model.addAttribute("findPasswordRequest", new FindPasswordRequest());
        return "user/find/pw";
    }

    // 비밀번호 찾기 처리
    @PostMapping("/find/password")
    public String findPassword(@ModelAttribute("findPasswordRequest") FindPasswordRequest findPasswordRequest,
                               RedirectAttributes redirectAttributes) {
        String userId = findPasswordRequest.getUserId();
        String userEmail = findPasswordRequest.getUserEmail();

        try {
            User user = userService.findByUserIdAndUserEmail(userId, userEmail);

            if (user != null) {
                String temporaryPassword = userService.generateTemporaryPassword();
                userService.updateUserPassword(user, temporaryPassword);
                emailService.sendTemporaryPasswordEmail(userEmail, temporaryPassword);
                redirectAttributes.addFlashAttribute("resetPasswordMessage", "임시 비밀번호가 이메일로 전송되었습니다.");
                logger.info("Temporary password sent to email: {}", userEmail);

                return "redirect:/user/home";
            } else {
                redirectAttributes.addFlashAttribute("errorMessage", "해당 사용자 아이디와 이메일을 찾을 수 없습니다.");
                logger.warn("User not found for username: {} and email: {}", userId, userEmail);
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "요청 처리 중 오류가 발생했습니다. 다시 시도해 주세요.");
            logger.error("Error during password reset request processing", e);
        }
        return "redirect:/find-pw"; // 오류 발생 시 올바른 경로로 리디렉트
    }

    // 비밀번호 재설정 처리 (토큰 없이)
    @GetMapping("/reset/userPw")
    public String showResetPasswordForm(Model model) {
        model.addAttribute("resetPasswordRequest", new ResetPasswordRequest());
        return "user/find/resetPassword";
    }

    // 비밀번호 재설정 처리 (토큰 없이)
    @PostMapping("/reset/userPw")
    public String resetPassword(@ModelAttribute("resetPasswordRequest") ResetPasswordRequest resetPasswordRequest,
                                RedirectAttributes redirectAttributes,
                                Model model) {
        String userId = resetPasswordRequest.getUserId();
        String newPassword = resetPasswordRequest.getPassword();

        // 비밀번호 일치 검사
        if (!newPassword.equals(resetPasswordRequest.getConfirmPassword())) {
            model.addAttribute("passwordMismatchError", "비밀번호가 일치하지 않습니다.");
            model.addAttribute("resetPasswordRequest", resetPasswordRequest);
            return "user/find/resetPassword";
        }

        try {
            boolean resetSuccess = userService.resetPasswordByUserId(userId, newPassword);

            if (resetSuccess) {
                redirectAttributes.addFlashAttribute("resetSuccessMessage", "비밀번호가 성공적으로 재설정되었습니다!");
                return "redirect:/user/home"; // 여기서 경로 변경
            } else {
                redirectAttributes.addFlashAttribute("errorMessage", "비밀번호 재설정에 실패했습니다. 다시 시도해 주세요.");
                return "redirect:/reset";
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "요청 처리 중 오류가 발생했습니다. 다시 시도해 주세요.");
            logger.error("Error during password reset", e);
            return "redirect:/reset";
        }
    }


    // 예외 처리 핸들러
    @ExceptionHandler(Exception.class)
    public String handleException(Exception e, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("errorMessage", "예상치 못한 오류가 발생했습니다. 다시 시도해 주세요.");
        logger.error("Unhandled exception", e);
        return "redirect:/find-pw";
    }
}