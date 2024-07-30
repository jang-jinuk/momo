package com.momo.momopjt.user;


import com.momo.momopjt.report.ReportDTO;
import com.momo.momopjt.report.ReportService;
import com.momo.momopjt.user.find.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
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

import static com.momo.momopjt.user.QUser.user;


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
  private final PasswordEncoder passwordEncoder;

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

  @GetMapping("/checkUserIdDuplicate")
  @ResponseBody
  public boolean checkUserIdDuplicate(@RequestParam("userId") String userId) {
    return userRepository.existsByUserId(userId);
  }

  @GetMapping("/checkUserNicknameDuplicate")
  @ResponseBody
  public boolean checkUserNicknameDuplicate(@RequestParam("userNickname") String userNickname) {
    return userRepository.existsByUserNickname(userNickname);
  }

  @GetMapping("/checkUserEmailDuplicate")
  @ResponseBody
  public boolean checkUserEmailDuplicate(@RequestParam("userEmail") String userEmail) {
    return userRepository.existsByUserEmail(userEmail);
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

    } catch (UserService.UserNicknameException e) {
      redirectAttributes.addFlashAttribute("errorNickname", "userNickname");
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
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    String currentUsername = null;

    // 인증된 사용자의 사용자 이름을 가져옴
    if (authentication instanceof UsernamePasswordAuthenticationToken) {
      Object principal = authentication.getPrincipal();
      if (principal instanceof UserSecurityDTO) {
        UserSecurityDTO userSecurityDTO = (UserSecurityDTO) principal;
        currentUsername = userSecurityDTO.getUsername(); // Get user ID
      }
    } else if (authentication instanceof OAuth2AuthenticationToken) {
      OAuth2AuthenticationToken oauth2Token = (OAuth2AuthenticationToken) authentication;
      DefaultOAuth2User oAuth2User = (DefaultOAuth2User) oauth2Token.getPrincipal();
      currentUsername = oAuth2User.getAttribute("email"); // Get email
    }

    if (currentUsername == null) {
      throw new SecurityException("사용자가 인증되지 않았습니다.");
    }

    // 현재 로그인한 사용자 정보 가져오기
    User currentUser = userRepository.findByUserId(currentUsername);
    if (currentUser == null) {
      throw new SecurityException("현재 사용자 정보를 찾을 수 없습니다.");
    }

    // 업데이트할 사용자 정보 가져오기
    User user = userRepository.findByUserId(userId);
    if (user == null) {
      throw new IllegalArgumentException("userId에 해당하는 사용자를 찾을 수 없습니다: " + userId);
    }

    // 현재 로그인한 사용자 또는 관리자 권한이 있는 사용자만 업데이트 가능
    if (!userId.equals(currentUser.getUserId()) && !currentUser.getUserRole().equals(UserRole.ADMIN)) {
      throw new SecurityException("현재 사용자에게는 해당 사용자의 정보를 업데이트할 권한이 없습니다.");
    }

    // 사용자 정보를 DTO로 변환
    ModelMapper modelMapper = new ModelMapper();
    UserDTO userDTO = modelMapper.map(user, UserDTO.class);

    // DTO를 모델에 추가
    model.addAttribute("userDTO", userDTO);

    return "user/update"; // Thymeleaf 템플릿 경로
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

    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    String currentUsername = null;

    if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
      currentUsername = ((UserDetails) authentication.getPrincipal()).getUsername();
    }

    // 현재 로그인한 사용자 정보 가져오기
    User currentUser = userRepository.findByUserId(currentUsername);

    if (currentUser == null || !userId.equals(currentUser.getUserId())) {
      redirectAttributes.addFlashAttribute("error", "현재 사용자에게는 해당 사용자의 정보를 업데이트할 권한이 없습니다.");
      return "redirect:/user/update/" + userId;
    }

    try {
      // 사용자 정보 업데이트
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
  public String deleteAccountPage(Model model, HttpServletRequest request) {
    // 현재 로그인된 사용자 정보 가져오기
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    UserSecurityDTO user = (UserSecurityDTO) authentication.getPrincipal();

    model.addAttribute("userId", user.getUsername());
    model.addAttribute("userSocial", user.getUserSocial());

    return "user/delete-account";
  }


  @PostMapping("/delete-account")
  public String deleteAccount(HttpServletRequest request, HttpServletResponse response,
                              @RequestParam String userId, @RequestParam(required = false) String userPw,
                              RedirectAttributes redirectAttributes) {
    try {
      // 사용자 정보를 가져옵니다
      User user = userRepository.findByUserId(userId);

      if (user == null) {
        throw new IllegalArgumentException("User not found");
      }

      // 소셜 로그인 사용자인지 확인합니다
      if (user.getUserSocial() != null && user.getUserSocial() != ' ') {
        // 소셜 로그인 사용자일 경우, 비밀번호는 기본값으로 설정합니다
        if ("1111".equals(userPw)) {
          userService.deleteAccount(userId, userPw);
          log.info("Social login user account has been deleted.");

          // 로그아웃 처리
          Authentication auth = SecurityContextHolder.getContext().getAuthentication();
          if (auth != null) {
            new SecurityContextLogoutHandler().logout(request, response, auth);
          }

          redirectAttributes.addFlashAttribute("message", "Your account has been deleted successfully.");
          return "redirect:/home";
        } else {
          throw new IllegalArgumentException("Invalid password for social login user");
        }
      } else {
        // 일반 사용자인 경우, 실제 비밀번호와 비교합니다
        if (userPw != null && passwordEncoder.matches(userPw, user.getUserPw())) {
          userService.deleteAccount(userId, userPw);
          log.info("User account has been deleted.");

          // 로그아웃 처리
          Authentication auth = SecurityContextHolder.getContext().getAuthentication();
          if (auth != null) {
            new SecurityContextLogoutHandler().logout(request, response, auth);
          }

          redirectAttributes.addFlashAttribute("message", "Your account has been deleted successfully.");
          return "redirect:/home";
        } else {
          throw new IllegalArgumentException("Incorrect password");
        }
      }
    } catch (Exception e) {
      log.error("Error deleting account: {}", e.getMessage());
      redirectAttributes.addFlashAttribute("message", "Error deleting account: " + e.getMessage());
      return "redirect:/user/delete-account";
    }

  }


  @PostMapping("/submitCategory")
  public String submitCategory(@RequestParam("userCategory") String userCategory,
                               RedirectAttributes redirectAttributes) {

    // UserDTO 객체 생성 및 userCategory 설정
    UserDTO userDTO = new UserDTO();
    userDTO.setUserCategory(userCategory);

    // 여기서 userDTO를 사용하여 필요한 작업을 수행합니다 (예: 데이터베이스 저장)

    redirectAttributes.addFlashAttribute("result", "success");
    return "redirect:/home"; // 성공 시 홈으로 리다이렉트
  }



  @GetMapping("/profile/my-report")
  public String myReportGet(Model model){
    //@RequestParam(value = "page", defaultValue = "1") int page) {
    log.info("...... [GET profile/my-report]..........KSW");
    // ID를 조회하여 모델에 추가 (임시)
    User user = new User();
    //SecurityContextHolder.getContext().getAuthentication().getPrincipal(); //로그인 정보 가져옴
    user.setUserNo(4L);
    //TODO 로그인된 사용자 정보 불러와서 아이디 넣어줘야 함 그 후 페이징 처리 SW
    List<ReportDTO> sandIdReport = reportService.readReport(user);

    // int totalReports = sandIdReport.size(); // 총 데이터 수
    // int pageSize = 10; // 한 번에 표시할 페이지 수
    // int lastPage = (totalReports + pageSize - 1) / pageSize; // 총 페이지 수

    // 페이지에 맞게 데이터 나누기
//    int fromIndex = (page - 1) * pageSize;
//    int toIndex = Math.min(fromIndex + pageSize, totalReports);
//    List<ReportDTO> reports = sandIdReport.subList(fromIndex, toIndex);

    // 페이지 그룹 계산
//    int pageGroupSize = 10; // 한 번에 표시할 페이지 번호 수
//    int currentGroup = (page - 1) / pageGroupSize;
//    int startPage = currentGroup * pageGroupSize + 1;
//    int endPage = Math.min(startPage + pageGroupSize - 1, lastPage);
    model.addAttribute("reportDTO", sandIdReport);
//    model.addAttribute("page", page);
//    model.addAttribute("lastPage", lastPage);
//    model.addAttribute("startPage", startPage);
//    model.addAttribute("endPage", endPage);

    log.info("...... [userIdReport]..........KSW");
    return "user/profile/my-report";  // 뷰 반환
  }

}

