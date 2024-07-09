package com.momo.momopjt.schedule;

import com.momo.momopjt.club.Club;
import com.momo.momopjt.user.User;
import com.momo.momopjt.userandschedule.UserAndScheduleDTO;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

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
  public String scheduleCreate(ScheduleDTO scheduleDTO, Long userNo, String dateTime) {
    User user = new User();
    user.setUserNo(userNo);
    Club club = new Club();
    club.setClubNo(1L);
    scheduleDTO.setClubNo(club);
    UserAndScheduleDTO userAndScheduleDTO = new UserAndScheduleDTO();
    userAndScheduleDTO.setUserNo(user);

    LocalDateTime localDateTime = LocalDateTime.parse(dateTime);
    ZonedDateTime zonedDateTime = localDateTime.atZone(ZoneId.systemDefault());
    Instant instant = zonedDateTime.toInstant();
    scheduleDTO.setScheduleStartDate(instant);
    log.info("------------ [날짜/시간 포매팅 완료] ------------");

    Long scheduleNo= scheduleService.createSchedule(scheduleDTO,userAndScheduleDTO);
    log.info("------------ [일정 등록 완료] ------------");
    return "/schedule/" + scheduleNo;
  }
}
