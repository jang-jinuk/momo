package com.momo.momopjt.schedule;

import com.momo.momopjt.club.Club;
import com.momo.momopjt.user.User;
import com.momo.momopjt.userandschedule.UserAndScheduleDTO;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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

  //일정 생성 페이지 이동
  @GetMapping("/create")
  public String scheduleCreate() {
    return "/schedule/create";
  }

  //일정 생성하기
  @PostMapping("/create")
  public String scheduleCreate(ScheduleDTO scheduleDTO, Long userNo, String dateTime) {
    User user = new User();
    user.setUserNo(1L); //임시 유저 정보 (유저 관련 기능 완성 후 추가할 예정)
    Club club = new Club(); // 임시 모임 정보(수정예정)
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

  //일정 상세페이지 이동
  @GetMapping("/{scheduleNo}")
  public String scheduleView(@PathVariable("scheduleNo") Long scheduleNo, Model model) {
    ScheduleDTO scheduleDTO = scheduleService.findSchedule(scheduleNo);
    model.addAttribute("scheduleDTO", scheduleDTO);
    return "/schedule/view";
  }
}
