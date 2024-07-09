package com.momo.momopjt.schedule;

import com.momo.momopjt.userandschedule.UserAndScheduleDTO;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Log4j2
@RequestMapping("/schedule")
public class ScheduleController {

  @Autowired
  ScheduleService scheduleService;

  @GetMapping("/create")
  public String scheduleCreate() {
    return "/schedule/create";
  }

  @PostMapping("/create")
  public String scheduleCreate(ScheduleDTO scheduleDTO, UserAndScheduleDTO userAndScheduleDTO) {
    Long scheduleNo= scheduleService.createSchedule(scheduleDTO,userAndScheduleDTO);
    return "/schedule/{scheduleNo}";
  }
}
