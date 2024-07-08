package com.momo.momopjt.club;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Log4j2
@RequestMapping("/club")
public class ClubController {
  @GetMapping("/main")
  public String mainPage() {
    log.info("------------ [club main] ------------");
    return "club/main";
  }

}
