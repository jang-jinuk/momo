package com.momo.momopjt.home;

import com.momo.momopjt.club.ClubDTO;
import com.momo.momopjt.club.ClubService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@Log4j2
public class HomeController {

  @Autowired
  private ClubService clubService;

  @GetMapping("/home")
  public String home(Model model) {
    List<ClubDTO> clubDTOList = clubService.readAllClub();
    model.addAttribute("clubDTOList", clubDTOList);
    return "/home";
  }
}
