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


    @PostMapping("/submit")
    public String handleFormSubmit(
        @RequestParam("userId") String userId,
        @RequestParam("userNickname") String userNickname,
        @RequestParam("userPw") String userPw,
        @RequestParam("userGender") char userGender,
        @RequestParam("userBirth") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate userBirth,
        @RequestParam("userEmail") String userEmail,
        @RequestParam("userCategory") String userCategory,
        @RequestParam("userAddress") String userAddress,
        @RequestParam("userMbti") String userMbti,
        Model model) {

        // 유효성 검사 패턴
        Pattern userIdPattern = Pattern.compile("^[a-zA-Z0-9]{5,15}$");
        Pattern emailPattern = Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");
        Pattern passwordPattern = Pattern.compile("^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[!@#$%^&*(),.?\":{}|<>])[a-zA-Z\\d!@#$%^&*(),.?\":{}|<>]{8,20}$");

        // Matcher 객체 생성
        Matcher userIdMatcher = userIdPattern.matcher(userId);
        Matcher emailMatcher = emailPattern.matcher(userEmail);
        Matcher passwordMatcher = passwordPattern.matcher(userPw);

        // 사용자 ID 유효성 검사
        if (!userIdMatcher.matches()) {
            model.addAttribute("errorMessage", "사용자 ID는 5~15자의 영문자와 숫자로만 구성되어야 합니다.");
            return "error";
        }

        // 이메일 유효성 검사
        if (!emailMatcher.matches()) {
            model.addAttribute("errorMessage", "이메일 형식이 유효하지 않습니다.");
            return "error";
        }

        // 비밀번호 유효성 검사
        if (!passwordMatcher.matches()) {
            model.addAttribute("errorMessage", "비밀번호는 8~20자 사이여야 하며, 영문자, 숫자, 특수문자 중 2종류 이상을 포함해야 합니다.");
            return "error";
        }

        // 닉네임 유효성 검사
        if (userNickname == null || userNickname.isEmpty()) {
            model.addAttribute("errorMessage", "닉네임이 필요합니다.");
            return "error";
        }

        // 성별 유효성 검사
        if (userGender != 'm' && userGender != 'w') {
            model.addAttribute("errorMessage", "성별이 유효하지 않습니다.");
            return "error";
        }

        // 생년월일 유효성 검사
        if (userBirth == null) {
            model.addAttribute("errorMessage", "생년월일이 필요합니다.");
            return "error";
        }
        /*
        // 카테고리 유효성 검사 (필요한 경우 추가)
        if (userCategory == null || userCategory.isEmpty()) {
            model.addAttribute("errorMessage", "카테고리가 필요합니다.");
            return "error";
        }

        // 주소 유효성 검사 (필요한 경우 추가)
        if (userAddress == null || userAddress.isEmpty()) {
            model.addAttribute("errorMessage", "주소가 필요합니다.");
            return "error";
        }

        // MBTI 유효성 검사 (필요한 경우 추가)
        if (userMbti == null || userMbti.isEmpty()) {
            model.addAttribute("errorMessage", "MBTI가 필요합니다.");
            return "error";
        }

         */

        // DTO 객체 생성 및 필드 설정
        UserJoinDTO userJoinDTO = new UserJoinDTO();
        userJoinDTO.setUserId(userId);
        userJoinDTO.setUserNickname(userNickname);
        userJoinDTO.setUserPw(userPw);
        userJoinDTO.setUserGender(userGender);
        userJoinDTO.setUserBirth(userBirth);
        userJoinDTO.setUserEmail(userEmail);
        userJoinDTO.setUserCategory(userCategory);
        userJoinDTO.setUserAddress(userAddress);
        userJoinDTO.setUserMbti(userMbti);

        // 사용자 가입 처리
        try {
            userService.join(userJoinDTO);
        } catch (UserService.UserIdException e) {
            model.addAttribute("errorMessage", "이미 존재하는 사용자 ID입니다.");
            return "error";
        }

        return "redirect:/user/editProfile";
    }
}