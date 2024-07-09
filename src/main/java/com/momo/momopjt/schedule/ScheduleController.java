package com.momo.momopjt.schedule;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Log4j2
@RequestMapping("/schedule")
public class ScheduleController {

  @GetMapping("/create")
  public String scheduleCreate() {
    return "/schedule/create";
  }
}
