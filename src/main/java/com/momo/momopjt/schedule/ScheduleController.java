package com.momo.momopjt.schedule;

import com.momo.momopjt.club.Club;
import com.momo.momopjt.club.ClubService;
import com.momo.momopjt.user.User;
import com.momo.momopjt.user.UserDTO;
import com.momo.momopjt.user.UserService;
import com.momo.momopjt.userandschedule.UserAndScheduleDTO;
import com.momo.momopjt.userandschedule.UserAndScheduleService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

@Controller
@Log4j2
@RequestMapping("/schedule")
public class ScheduleController {

  @Autowired
  ScheduleService scheduleService;
  private UserService userService;
  private UserAndScheduleService userAndScheduleService;

  //일정 생성 페이지 이동
  @GetMapping("/create")
  public String scheduleCreate() {
    return "/schedule/create";
  }

  //일정 생성하기
  @PostMapping("/create")
  public String scheduleCreate(ScheduleDTO scheduleDTO, String dateTime, HttpSession session) {

    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    String username = auth.getName();
    User user = userService.findByUserId(username);
    UserAndScheduleDTO userAndScheduleDTO = new UserAndScheduleDTO();
    userAndScheduleDTO.setUserNo(user);
    log.info("------------ [현재 로그인 중인 정보] ------------");

    Long clubNo = (Long) session.getAttribute("clubNo");
    Club club = new Club(); // 임시 모임 정보(수정예정)
    club.setClubNo(clubNo);
    scheduleDTO.setClubNo(club);
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
    Schedule schedule = new Schedule();
    schedule.setScheduleNo(scheduleNo);
    List<UserDTO> userDTOList = userAndScheduleService.readAllParticipants(schedule);
    model.addAttribute("userDTOList", userDTOList);
    return "/schedule/view";
  }
}
