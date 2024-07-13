package com.momo.momopjt.schedule;

import com.momo.momopjt.club.Club;
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
  @Autowired
  private UserService userService;
  @Autowired
  private UserAndScheduleService userAndScheduleService;
  @Autowired
  private HttpSession session;

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
    Club club = new Club();
    club.setClubNo(clubNo);
    scheduleDTO.setClubNo(club);
    LocalDateTime localDateTime = LocalDateTime.parse(dateTime);
    ZonedDateTime zonedDateTime = localDateTime.atZone(ZoneId.systemDefault());
    Instant instant = zonedDateTime.toInstant();
    scheduleDTO.setScheduleStartDate(instant);
    log.info("------------ [날짜/시간 포매팅 완료] ------------");

    Long scheduleNo= scheduleService.createSchedule(scheduleDTO,userAndScheduleDTO);
    log.info("------------ [일정 등록 완료] ------------");

    return "redirect:/schedule/" + scheduleNo;
  }

  //일정 상세페이지 이동
  @GetMapping("/{scheduleNo}")
  public String scheduleView(@PathVariable("scheduleNo") Long scheduleNo, Model model) {
    //일정 조회
    ScheduleDTO scheduleDTO = scheduleService.findSchedule(scheduleNo);

    //인원마감인지 확인
    Boolean isScheduleFull = scheduleService.isScheduleFull(scheduleNo);

    //참가인원 조회
    Schedule schedule = new Schedule();
    schedule.setScheduleNo(scheduleNo);
    List<UserDTO> userDTOList = userAndScheduleService.readAllParticipants(schedule);

    //현재 로그인된 회원이 일정에 참석했는지 확인
    UserAndScheduleDTO userAndScheduleDTO = new UserAndScheduleDTO();
    userAndScheduleDTO.setScheduleNo(schedule);

    //현재 로그인한 회원 정보 조회하는 로직 메서드로 따로 분리할 건지 생각해보기
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    String username = auth.getName();
    User user = userService.findByUserId(username);
    userAndScheduleDTO.setUserNo(user);

    int isParticipant = userAndScheduleService.isParticipanting(userAndScheduleDTO);

    model.addAttribute("scheduleDTO", scheduleDTO); //일정 정보
    model.addAttribute("isScheduleFull", isScheduleFull); //일정인원 마감 여부
    model.addAttribute("userDTOList", userDTOList); //참가자 정보
    model.addAttribute("isParticipant", isParticipant); //현재 로그인한 회원이 해당 일정에 참석했는지 여부
    session.setAttribute("scheduleNo", scheduleNo); //일정 번호

    return "/schedule/view";
  }

  @GetMapping("/join")
  public String attendSchedule(Model model, HttpSession session) {
    Long scheduleNo = (Long) session.getAttribute("scheduleNo");

    //현재 로그인한 회원 정보 조회하는 로직 메서드로 따로 분리할 건지 생각해보기
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    String username = auth.getName();
    User user = userService.findByUserId(username);
    log.info("------------ [현재 로그인 중인 회원 정보 조회] ------------");
    UserAndScheduleDTO userAndScheduleDTO = new UserAndScheduleDTO();
    userAndScheduleDTO.setUserNo(user);
    log.info("------------ [회원 정보 전달] ------------");

    String message = scheduleService.joinSchedule(scheduleNo, userAndScheduleDTO);
    model.addAttribute("message", message);

    return "redirect:/schedule/" + scheduleNo;
  }

  @GetMapping("/leave")
  public String absentSchedule(Model model, HttpSession session) {
    Long scheduleNo = (Long) session.getAttribute("scheduleNo");

    //현재 로그인한 회원 정보 조회하는 로직 메서드로 따로 분리할 건지 생각해보기
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    String username = auth.getName();
    User user = userService.findByUserId(username);
    log.info("------------ [현재 로그인 중인 회원 정보 조회] ------------");
    UserAndScheduleDTO userAndScheduleDTO = new UserAndScheduleDTO();
    userAndScheduleDTO.setUserNo(user);
    log.info("------------ [회원 정보 전달] ------------");

    String message = scheduleService.leaveSchedule(scheduleNo, userAndScheduleDTO);
    model.addAttribute("message", message);

    return "redirect:/schedule/" + scheduleNo;
  }
}
