package com.momo.momopjt.club;

import com.momo.momopjt.photo.PhotoDTO;
import com.momo.momopjt.schedule.ScheduleDTO;
import com.momo.momopjt.schedule.ScheduleService;
import com.momo.momopjt.user.User;
import com.momo.momopjt.user.UserService;
import com.momo.momopjt.userandclub.UserAndClubDTO;
import com.momo.momopjt.userandclub.UserAndClubService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.time.Instant;
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
  private UserService userService;
  @Autowired
  private UserAndClubService userAndClubService;
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

    //TODO 현재 로그인한 회원 정보 조회하는 로직 메서드로 따로 분리할 건지 생각해보기
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    String username = auth.getName();
    User user = userService.findByUserId(username);
    UserAndClubDTO userAndClubDTO =  new UserAndClubDTO();
    userAndClubDTO.setUserNo(user);
    userAndClubDTO.setClubNo(club);

    Boolean isLeader = userAndClubService.isLeader(userAndClubDTO); //모임장인지 확인
    model.addAttribute("isLeader", isLeader);

    return "club/main";
  }
  @GetMapping("/create")
  public String createClub() {return "club/create";}

  @PostMapping("/create")
  public String createClub(ClubDTO clubDTO, PhotoDTO photoDTO) {

    //TODO 현재 로그인한 회원 정보 조회하는 로직 메서드로 따로 분리할 건지 생각해보기
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    String username = auth.getName();
    User user = userService.findByUserId(username);
    UserAndClubDTO userAndClubDTO = new UserAndClubDTO();
    userAndClubDTO.setUserNo(user);
    userAndClubDTO.setJoinDate(Instant.now());

    //TODO 파일 업로드 기능과 연결필요

    Long clubNo = clubService.createClub(clubDTO,photoDTO,userAndClubDTO);

    return "redirect:/club/main/" + clubNo;
  }

  @GetMapping("/update")
  public String updateClub(Model model, HttpSession session) {
    Long clubNo = (Long) session.getAttribute("clubNo");
    ClubDTO clubDTO = clubService.readOneClub(clubNo);
    model.addAttribute("clubDTO", clubDTO);
    return "/club/update";
  }
  @PostMapping("/update")
  public String updateClub(ClubDTO clubDTO, PhotoDTO photoDTO, RedirectAttributes redirectAttributes) {
    Long clubNo = (Long) session.getAttribute("clubNo");
    clubDTO.setClubNo(clubNo);

    Boolean isSuccess = clubService.updateClub(clubDTO, photoDTO);

    if (isSuccess) {
      redirectAttributes.addFlashAttribute("message","모임 수정이 완료되었습니다.");
      return "redirect:/club/main/" + clubDTO.getClubNo();
    }
    redirectAttributes.addFlashAttribute("message","모임 구성원 수보다 적게 수정할 수 없습니다.");
    return "redirect:/club/update";
  }
}
