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
import org.springframework.web.bind.annotation.*;
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

  //모임 메인페이지 조회
  @GetMapping("/main/{clubNo}")
  public String mainPage(@PathVariable("clubNo") Long clubNo, Model model, HttpSession session) {
    log.info("------------ [club main] ------------");
    
    ClubDTO clubDTO = clubService.readOneClub(clubNo);
    model.addAttribute("club", clubDTO);
    log.info("------------clubNo {}------------",clubDTO.getClubNo());

    Club club = new Club();
    club.setClubNo(clubNo);
    List<ScheduleDTO> endSchedules = scheduleService.getEndSchedules(club); //마감된 일정
    model.addAttribute("endSchedules", endSchedules);
    List<ScheduleDTO> getOngoingSchedules= scheduleService.getOngoingSchedules(club);//마감되지 않은 일정
    model.addAttribute("getOngoingSchedules", getOngoingSchedules);
    log.info("------------ [found schedules] ------------");

    session.setAttribute("clubNo", clubDTO.getClubNo());
    //세션에 모임 clubNo을 저장하고 해당 모임 일정 및 게시글 처리시 사용

    //TODO 현재 로그인한 회원 정보 조회하는 로직 메서드로 따로 분리할 건지 생각해보기 JW
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    String username = auth.getName();
    User user = userService.findByUserId(username);
    UserAndClubDTO userAndClubDTO =  new UserAndClubDTO();
    userAndClubDTO.setUserNo(user);
    userAndClubDTO.setClubNo(club);

    int isMember = userAndClubService.isMember(userAndClubDTO); //모임에 소속되었는지 확인
    if (isMember == 0 || isMember == 1) {
      return "redirect:/club/join-page";
    }
    model.addAttribute("isMember", isMember);

    return "/club/main";
  }
  @GetMapping("/create")
  public String createClub() {return "/club/create";}

  //모임 생성
  @PostMapping("/create")
  public String createClub(ClubDTO clubDTO, PhotoDTO photoDTO, RedirectAttributes redirectAttributes) {

    //TODO 현재 로그인한 회원 정보 조회하는 로직 메서드로 따로 분리할 건지 생각해보기 JW
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    String username = auth.getName();
    User user = userService.findByUserId(username);
    UserAndClubDTO userAndClubDTO = new UserAndClubDTO();
    userAndClubDTO.setUserNo(user);
    userAndClubDTO.setJoinDate(Instant.now());

    //TODO 파일 업로드 기능과 연결필요 JW

    Long clubNo = clubService.createClub(clubDTO, photoDTO, userAndClubDTO); //TODO club create error

    redirectAttributes.addFlashAttribute("message", "축하합니다! 모임이 생성되었습니다!");

    return "redirect:/club/main/" + clubNo;
  }

  @GetMapping("/update")
  public String updateClub(Model model, HttpSession session) {
    Long clubNo = (Long) session.getAttribute("clubNo");
    ClubDTO clubDTO = clubService.readOneClub(clubNo);
    model.addAttribute("clubDTO", clubDTO);
    return "/club/update";
  }

  //모임 수정
  @PostMapping("/update")
  public String updateClub(ClubDTO clubDTO, PhotoDTO photoDTO, RedirectAttributes redirectAttributes, HttpSession session) {
    Long clubNo = (Long) session.getAttribute("clubNo");
    clubDTO.setClubNo(clubNo);

    Boolean isSuccess = clubService.updateClub(clubDTO, photoDTO);

    if (isSuccess) {
      redirectAttributes.addFlashAttribute("message","모임 수정이 완료되었습니다.");
      return "redirect:/club/main/" + clubNo;
    }
    redirectAttributes.addFlashAttribute("message","모임 구성원 수보다 적게 수정할 수 없습니다.");
    return "redirect:/club/update";
  }

  @GetMapping("/disband-page")
  public String goDisbandPage() {
    return "/club/disband";
  }

  //모임 해산
  @GetMapping("/disband")
  public String disbandClub(HttpSession session, RedirectAttributes redirectAttributes) {
    Long clubNo = (Long) session.getAttribute("clubNo");
    clubService.disbandClub(clubNo);
    session.removeAttribute("clubNo");
    redirectAttributes.addFlashAttribute("message","모임이 해산되었습니다.");
    return "redirect:/user/home";
  }

  @GetMapping("/leavePage")
  public String goLeavePage() {
    return "/club/leave";
  }

  //모임 탈퇴
  @GetMapping("/leave")
  public String leaveClub(@RequestParam(value = "userNo", required = false)Long userNo, HttpSession session, RedirectAttributes redirectAttributes) {
    Long clubNo = (Long) session.getAttribute("clubNo");
    Club club = new Club();
    club.setClubNo(clubNo);

    User user = new User();
    user.setUserNo(userNo);

    UserAndClubDTO userAndClubDTO =  new UserAndClubDTO();
    userAndClubDTO.setClubNo(club);

    if (userNo != null) { //모임장이 모임원을 탈퇴 시킬 때
      userAndClubDTO.setUserNo(user);
      userAndClubService.leaveClub(userAndClubDTO);
      redirectAttributes.addFlashAttribute("message","모임원이 삭제 되었습니다.");
      return "redirect:/club/members";
    }

    //TODO 현재 로그인한 회원 정보 조회하는 로직 메서드로 따로 분리할 건지 생각해보기 JW
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    String username = auth.getName();
    user = userService.findByUserId(username);
    userAndClubDTO.setUserNo(user);

    userAndClubService.leaveClub(userAndClubDTO);

    session.removeAttribute("clubNo");

    redirectAttributes.addFlashAttribute("message","모임에서 정상적으로 탈퇴되었습니다.");

    return "redirect:/user/home";
  }

  @GetMapping("/join-page")
  public String goJoinPage(HttpSession session, Model model) {
    Long clubNo = (Long) session.getAttribute("clubNo");
    Club club = new Club();
    club.setClubNo(clubNo);

    //TODO 현재 로그인한 회원 정보 조회하는 로직 메서드로 따로 분리할 건지 생각해보기 JW
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    String username = auth.getName();
    User user = userService.findByUserId(username);

    UserAndClubDTO userAndClubDTO =  new UserAndClubDTO();
    userAndClubDTO.setUserNo(user);
    userAndClubDTO.setClubNo(club);
    int isMember = userAndClubService.isMember(userAndClubDTO);

    UserAndClubDTO isLeader = userAndClubService.isLeader(club);
    ClubDTO clubDTO = clubService.readOneClub(clubNo);
    List<UserAndClubDTO> userAndClubDTOS = userAndClubService.readAllMembers(club);
    int countMembers = userAndClubService.countMembers(club);

    model.addAttribute("isLeader", isLeader); //모임장 정보
    model.addAttribute("clubDTO", clubDTO); //모임 정보
    model.addAttribute("userAndClubDTOS", userAndClubDTOS); // 모임 맴버 정보
    model.addAttribute("countMembers", countMembers);//모임 맴버 인원 수
    model.addAttribute("isMember", isMember); //가입 신청 여부
    return "/club/join";
  }

  //모임 가입 신청
  @GetMapping("/join")
  public String joinClub(HttpSession session) {
    Long clubNo = (Long) session.getAttribute("clubNo");
    Club club = new Club();
    club.setClubNo(clubNo);

    //TODO 현재 로그인한 회원 정보 조회하는 로직 메서드로 따로 분리할 건지 생각해보기 JW
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    String username = auth.getName();
    User user = userService.findByUserId(username);

    UserAndClubDTO userAndClubDTO = new UserAndClubDTO();
    userAndClubDTO.setClubNo(club);
    userAndClubDTO.setUserNo(user);

    userAndClubService.joinClub(userAndClubDTO);

    return "redirect:/club/join-complete";
  }

  //가입 신청 완료
  @GetMapping("/join-complete")
  public String joinClubComplete(HttpSession session, Model model) {
    Long clubNo = (Long) session.getAttribute("clubNo");
    ClubDTO clubDTO= clubService.readOneClub(clubNo);
    model.addAttribute("clubDTO", clubDTO);
    return "/club/join-complete";
  }

  //맴버 관리
  @GetMapping("/members")
  public String goMembers(Model model, HttpSession session) {
    Long clubNo = (Long) session.getAttribute("clubNo");
    Club club = new Club();
    club.setClubNo(clubNo);
    List<UserAndClubDTO> userAndClubDTOS = userAndClubService.readAllMembers(club);
    List<UserAndClubDTO> joinList = userAndClubService.readAllJoinList(club);

    model.addAttribute("userAndClubDTOS", userAndClubDTOS);
    model.addAttribute("joinList", joinList);

    return "/club/members";
  }

  //가입 신청 승인
  @GetMapping("/approve-join")
  public String approveJoin(@RequestParam("userNo")Long userNo, HttpSession session,
                            RedirectAttributes redirectAttributes) {
    Long clubNo = (Long) session.getAttribute("clubNo");
    Club club = new Club();
    club.setClubNo(clubNo); //모임번호 지정

    User user = new User();
    user.setUserNo(userNo); //승인할 회원 지정

    UserAndClubDTO userAndClubDTO = new UserAndClubDTO();
    userAndClubDTO.setUserNo(user);
    userAndClubDTO.setClubNo(club);

    Boolean isSuccess = userAndClubService.approveJoin(userAndClubDTO);
    if(!isSuccess) {
      redirectAttributes.addFlashAttribute("message", "인원 초과입니다. 최대인원수를 수정해주세요");
      return "redirect:/club/members";
    }

    redirectAttributes.addFlashAttribute("message", "승인이 완료되었습니다.");
    return "redirect:/club/members";
  }
}
