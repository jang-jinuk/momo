package com.momo.momopjt.user;


import com.momo.momopjt.article.ArticleDTO;
import com.momo.momopjt.article.ArticleService;
import com.momo.momopjt.report.ReportDTO;
import com.momo.momopjt.report.ReportService;
import com.momo.momopjt.user.find.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;


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
  private final ReportService reportService;
  private final ArticleService articleService;

  @GetMapping("/login")
  public void loginGET(HttpServletRequest request, Model model) {
    log.info("----------------- [GET /login]-----------------");
    String errorCode = request.getParameter("errorCode");
    String logout = request.getParameter("logout");
    String expired = request.getParameter("expired");  // 세션 만료 여부 확인

    log.info("login get........");
    log.info("logout: " + logout);

    if (logout != null) {

      log.info("user logout......");
      model.addAttribute("message", "성공적으로 로그아웃되었습니다.");  // 로그아웃 메시지 추가
    }

    if (expired != null) {
      log.info("session expired......");
      model.addAttribute("message", "세션이 만료되었습니다. 다시 로그인해 주세요.");  // 세션 만료 메시지 추가
    }
  }

  @GetMapping("/signup")
  public String signupGet() {
    log.info("----Processing GET request for /signup");
    return "user/signup";
  }

  @PostMapping("/signup")
  public String signupPost(@Valid UserDTO userDTO, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
    log.info("----Processing POST request for /signup with data: {}", userDTO);

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
    return "redirect:/home"; // 회원가입 후 홈으로

  }

  @PostMapping("/logout")
  public String logout(HttpServletRequest request, RedirectAttributes redirectAttributes) {
    log.info("----------------- [POST /logout]-----------------");

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
    // userId에 해당하는 User 엔티티 조회
    User user = userRepository.findByUserId(userId);

    if (user == null) {
      throw new IllegalArgumentException("userId에 해당하는 사용자를 찾을 수 없습니다: " + userId);
    }
    ModelMapper modelMapper = new ModelMapper();
    UserDTO userDTO = modelMapper.map(user, UserDTO.class);
    model.addAttribute("userDTO", userDTO);

    return "user/update";
  }

  @PostMapping("/update")
  public String updatePost(@ModelAttribute("userDTO") @Valid UserDTO userDTO,
                           BindingResult bindingResult, RedirectAttributes redirectAttributes) {
    log.info("----Processing POST request for /update with data: {}", userDTO);
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
    return "redirect:/home";
  }

  @GetMapping("/delete-account")
  public String deleteAccountForm(Model model) {
    model.addAttribute("message", "");
    return "user/delete-account"; // 회원 탈퇴 폼 페이지로 이동
  }

  @PostMapping("/delete-account")
  public String deleteAccount(HttpServletRequest request, HttpServletResponse response,
                              Model model, @RequestParam String userId, @RequestParam String userPw) {
    try {
      userService.deleteAccount(userId, userPw); // 회원 탈퇴 서비스 호출

      // 로그아웃 처리
      Authentication auth = SecurityContextHolder.getContext().getAuthentication();
      if (auth != null) {
        new SecurityContextLogoutHandler().logout(request, response, auth);
      }

      model.addAttribute("message", "Your account has been deleted successfully.");
      return "redirect:/home"; // 탈퇴 처리 후 홈 페이지로 리다이렉트

    } catch (Exception e) {
      model.addAttribute("message", "Error deleting account: " + e.getMessage());
      return "user/delete-account"; // 에러 발생 시 다시 탈퇴 폼 페이지로
    }
  }

  //내 신고 페이지 조회/페이징
  @GetMapping("/profile/my-report")
  public String myReportGet(Model model,
                            @RequestParam(value = "page", defaultValue = "1") int page) {
    log.info("...... [GET profile/my-report]..........KSW");
    // 로그인된 사용자 정보 가져오기
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    String username = auth.getName(); // 사용자 이름 가져오기

    User user = userService.findByUserId(username); // 사용자 서비스에서 사용자 정보 조회
//    if (user == null) {
//      // 사용자 정보를 찾을 수 없는 경우 처리
//      model.addAttribute("error", "사용자를 찾을 수 없습니다.");
//      return "error"; // 에러 페이지로 리턴
//    }
    List<ReportDTO> reportDTOS = reportService.readReport(user);

    int totalReports = reportDTOS.size(); // 총 데이터 수
    int pageSize = 10; // 한 번에 표시할 페이지 수
    int lastPage = (totalReports + pageSize - 1) / pageSize; // 총 페이지 수

    // 페이지에 맞게 데이터 나누기
    int fromIndex = (page - 1) * pageSize;
    int toIndex = Math.min(fromIndex + pageSize, totalReports);
    List<ReportDTO> reports = reportDTOS.subList(fromIndex, toIndex);

    // 페이지 그룹 계산
    int pageGroupSize = 10; // 한 번에 표시할 페이지 번호 수
    int currentGroup = (page - 1) / pageGroupSize;
    int startPage = currentGroup * pageGroupSize + 1;
    int endPage = Math.min(startPage + pageGroupSize - 1, lastPage);

    model.addAttribute("reportDTOS", reports);
    model.addAttribute("page", page);
    model.addAttribute("lastPage", lastPage);
    model.addAttribute("startPage", startPage);
    model.addAttribute("endPage", endPage);

    log.info("...... [userIdReport]..........KSW");
    return "user/profile/my-report";  // 뷰 반환
  }

  //user 프로필 조회
  @GetMapping("/profile/dumyprofile/{userId}")
  public String getUserProfileGET(@PathVariable String userId, Model model) {
    log.info("...... [UserController/getUserProfileGET/running GET]..........KSW");

    // 사용자 정보 조회
    User user = userService.findByUserId(userId);

    // 사용자 정보가 존재하는지 확인
    if (user != null) {
      model.addAttribute("user", user);
      model.addAttribute("loggedInUserId", userId);

      // 현재 로그인한 사용자 정보 확인
      Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
      if (authentication != null && authentication.getPrincipal() instanceof UserSecurityDTO) {
        UserSecurityDTO userDetails = (UserSecurityDTO) authentication.getPrincipal();
        String loggedInUserId = userDetails.getUserId();

        model.addAttribute("loggedInUserId", loggedInUserId); // 로그인한 사용자 ID 설정
        model.addAttribute("isOwnProfile", loggedInUserId.equals(userId)); // 로그인한 사용자의 프로필인지 여부
      }

      // 사용자가 쓴 후기 조회
      List<ArticleDTO> userArticles = articleService.getAllArticlesByUser(user);
      model.addAttribute("userArticles", userArticles); // 모델에 후기 정보 추가

    } else {
      // 사용자 정보가 없는 경우의 처리
      log.warn("User not found for ID: {}", userId);
      return "error/userNotFound"; // 사용자 없음 처리 페이지 (없음)
    }
    return "user/profile/dumyprofile"; // 프로필 뷰 페이지 반환
  }
}