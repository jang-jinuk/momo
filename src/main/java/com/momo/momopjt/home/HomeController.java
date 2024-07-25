package com.momo.momopjt.home;

import com.momo.momopjt.club.ClubDTO;
import com.momo.momopjt.club.ClubService;
import com.momo.momopjt.user.User;
import com.momo.momopjt.user.UserService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import java.util.List;

@Controller
@Log4j2
public class HomeController {

  @Autowired
  private ClubService clubService;
  @Autowired
  private UserService userService;

    @GetMapping("/home")
    public String home(Model model) {
      Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
      if (authentication != null && authentication.isAuthenticated() && !authentication.getName().equals("anonymousUser")) {
        String userId = authentication.getName(); // 현재 로그인한 사용자의 ID
        User user = userService.findByUserId(userId); // User 객체를 가져옵니다
        if (user != null) {
          // 로그인한 사용자의 닉네임을 모델에 추가
          model.addAttribute("userNickname", user.getUserNickname()); // 또는 user.getNickname()이 적절한 방법입니다
        }
      }
      return "home"; // 홈 페이지의 Thymeleaf 템플릿 이름
    }

//  public String home(Model model) {
//
//
//    //TODO 현재 로그인한 회원 정보 조회하는 로직 메서드로 따로 분리할 건지 생각해보기 JW
//    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//    String username = auth.getName();
//    User user = userService.findByUserId(username);
//
//    List<ClubDTO> myClubDTOList = clubService.readMyClubs(user);
//    List<ClubDTO> clubDTOList = clubService.readAllClub();
//
//    model.addAttribute("myClubDTOList", myClubDTOList);
//    model.addAttribute("clubDTOList", clubDTOList);
//    return "home";
  }
