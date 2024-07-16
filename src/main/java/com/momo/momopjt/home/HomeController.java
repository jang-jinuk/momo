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


    //TODO 현재 로그인한 회원 정보 조회하는 로직 메서드로 따로 분리할 건지 생각해보기 JW
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    String username = auth.getName();
    User user = userService.findByUserId(username);

    List<ClubDTO> myClubDTOList = clubService.readMyClubs(user);
    List<ClubDTO> clubDTOList = clubService.readAllClub();

    model.addAttribute("myClubDTOList", myClubDTOList);
    model.addAttribute("clubDTOList", clubDTOList);
    return "/home";
  }
}
