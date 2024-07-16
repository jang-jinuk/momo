package com.momo.momopjt.club;

import com.momo.momopjt.schedule.ScheduleDTO;
import com.momo.momopjt.schedule.ScheduleService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import javax.servlet.http.HttpSession;
import java.util.List;


@Controller
@Log4j2
@RequestMapping("/club")
public class ClubController {

  @Autowired
  private ClubService clubService;
  @Autowired
  private ScheduleService scheduleService;
  @Autowired
  private HttpSession session;

  @GetMapping("/main/{clubNo}")
  public String mainPage(@PathVariable("clubNo") Long clubNo, Model model) {
    log.info("------------ [club main] ------------");
    
    ClubDTO clubDTO = clubService.readOneClub(clubNo);
    model.addAttribute("club", clubDTO);
    log.info("------------clubNo {}------------",clubDTO.getClubNo());

    Club club = new Club();
    club.setClubNo(clubNo);

    List<ScheduleDTO> scheduleDTOList = scheduleService.getOngoingSchedules(club);
    model.addAttribute("schedules", scheduleDTOList);
    log.info("------------ [found schedules] ------------");

    session.setAttribute("clubNo", clubDTO.getClubNo());
    //세션에 모임 clubNo을 저장하고 해당 모임 일정 및 게시글 처리시 사용
    return "club/main";
  }

}
