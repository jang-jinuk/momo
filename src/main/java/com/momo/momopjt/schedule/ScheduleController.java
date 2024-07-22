package com.momo.momopjt.schedule;

import com.momo.momopjt.club.Club;
import com.momo.momopjt.user.User;
import com.momo.momopjt.user.UserDTO;
import com.momo.momopjt.user.UserService;
import com.momo.momopjt.userandclub.UserAndClubService;
import com.momo.momopjt.userandschedule.UserAndScheduleDTO;
import com.momo.momopjt.userandschedule.UserAndScheduleService;
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
  @Autowired
  private UserAndClubService userAndClubService;

  //일정 생성 페이지 이동
  @GetMapping("/create")
  public String createScheduleGet(Model model) {
    log.info("------------ [Get schedule create] ------------");
    Long clubNo = (Long) session.getAttribute("clubNo");
    Club club = new Club();
    club.setClubNo(clubNo);
    int countMembers = userAndClubService.countMembers(club); //일정 생성 시 최대 참가 인원은 모임 전체 인원과 같음
    model.addAttribute("countMembers", countMembers);
    return "schedule/create";
  }

  //일정 생성하기
  @PostMapping("/create")
  public String scheduleCreatePost(ScheduleDTO scheduleDTO, String dateTime, HttpSession session) {
    log.info("------------ [Post schedule create] ------------");

    //이미지 파일 처리
    //1. form 받은 multipart 파일을 rest controller에 전달
    //2. rest controller에서 multipart 타입으로 저장 및 썸네일 생성
    //3. (Local)저장된 이미지 파일, 썸네일 파일을 byte[]파일로 변환해서 photo table에 저장
    //4. photo 테이블에 저장된 이미지 파일의 uuid 반환
//    String strUUID = photoService.savePhoto(PhotoDTO.builder().build()).getPhotoUUID();

     /* 주석처리

    //photo 저장에 필요한 user 정보 받아오는 공통 로직 앞으로 뺌
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    String username = auth.getName();
    User user = userService.findByUserId(username);


//    photoService.savePhoto();
//    이미지 파일 처리 YY
//    String resultPhotoUUID;
//    byte[] photoBytes = file.getBytes();
//    resultPhotoUUID = photoService.savePhoto(
//        PhotoDTO.builder()
//            .photoData(photoBytes)
//            .photoSize(file.getSize())
//            .photoOriginalName(file.getOriginalFilename())
//            .userNo(user)
//            .build())
//        .getPhotoUUID();


    //이미지 처리 후 UUID만 반환
    String resultPhotoUUID = UUID.randomUUID().toString();
    scheduleDTO.setSchedulePhoto(resultPhotoUUID);
    //이미지를 일정 DTO에 전달 끝

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
    Long scheduleNo;
    if (resultPhotoUUID != null) {
      scheduleNo = scheduleService.createSchedule(scheduleDTO, userAndScheduleDTO);
      return "redirect:/schedule/" + scheduleNo;
    }
    log.info("------------ [일정 등록 완료] ------------");
    return "redirect:/schedule";

*/
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
  public String readScheduleGet(@PathVariable("scheduleNo") Long scheduleNo, Model model) {
    log.info("------------ [Get schedule no: {}] ------------",scheduleNo);
    //일정 조회
    ScheduleDTO scheduleDTO = scheduleService.readOneSchedule(scheduleNo);

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

    int isParticipant = userAndScheduleService.isParticipate(userAndScheduleDTO);

    Long clubNo = (Long) session.getAttribute("clubNo");

    model.addAttribute("currentTime", Instant.now());
    model.addAttribute("clubNo",clubNo);
    model.addAttribute("scheduleDTO", scheduleDTO); //일정 정보
    model.addAttribute("isScheduleFull", isScheduleFull); //일정인원 마감 여부
    model.addAttribute("userDTOList", userDTOList); //참가자 정보
    model.addAttribute("isParticipant", isParticipant); //현재 로그인한 회원이 해당 일정에 참석했는지 여부
    session.setAttribute("scheduleNo", scheduleNo); //일정 번호

    return "schedule/view";
  }

  //일정 참가
  @GetMapping("/join")
  public String joinScheduleGet(RedirectAttributes redirectAttributes, HttpSession session) {
    log.info("------------ [Get schedule join] ------------");
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
    redirectAttributes.addFlashAttribute("message", message);

    return "redirect:/schedule/" + scheduleNo;
  }

  //일정 참가 취소
  @GetMapping("/leave")
  public String leaveScheduleGet(RedirectAttributes redirectAttributes, HttpSession session) {
    log.info("------------ [Get schedule leave] ------------");
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
    redirectAttributes.addFlashAttribute("message", message);

    return "redirect:/schedule/" + scheduleNo;
  }

  //일정 수정 페이지 이동
  @GetMapping("/update")
  public String updateScheduleGet(Model model, HttpSession session) {
    log.info("------------ [Get schedule update] ------------");
    Long scheduleNo = (Long) session.getAttribute("scheduleNo");
    ScheduleDTO scheduleDTO = scheduleService.readOneSchedule(scheduleNo);

    //날짜/시간 포매팅
    Instant originStartDate = scheduleDTO.getScheduleStartDate();
    ZonedDateTime zonedDate = originStartDate.atZone(ZoneId.systemDefault());
    LocalDateTime formattedDate = zonedDate.toLocalDateTime();

    Long clubNo = (Long) session.getAttribute("clubNo");
    Club club = new Club();
    club.setClubNo(clubNo);
    int countMembers = userAndClubService.countMembers(club); //일정 수정 시 최대 참가 인원은 모임 전체 인원과 같음
    model.addAttribute("countMembers", countMembers);
    model.addAttribute("scheduleStartDate", formattedDate);
    model.addAttribute("scheduleDTO", scheduleDTO);
    return "schedule/update";
  }

  //일정 수정
  @PostMapping("/update")
  public String updateSchedulePost(ScheduleDTO scheduleDTO, String dateTime, HttpSession session, RedirectAttributes redirectAttributes) {
    log.info("------------ [Post schedule update] ------------");
    Long scheduleNo = (Long) session.getAttribute("scheduleNo");
    scheduleDTO.setScheduleNo(scheduleNo);

    //날짜/시간 포매팅
    LocalDateTime localDateTime = LocalDateTime.parse(dateTime);
    ZonedDateTime zonedDateTime = localDateTime.atZone(ZoneId.systemDefault());
    Instant instant = zonedDateTime.toInstant();
    scheduleDTO.setScheduleStartDate(instant);

    Boolean updateFail = scheduleService.updateSchedule(scheduleDTO);

    //일정 수정 실패
    if (updateFail) {
      redirectAttributes.addFlashAttribute("message", "현재 참자가 수보다 작게 설정할 수 없습니다.");
      return "redirect:/schedule/update";
    }
    redirectAttributes.addFlashAttribute("message", "일정 수정이 완료되었습니다.");
    return "redirect:/schedule/" + scheduleNo;
  }

  //일정 삭제
  @GetMapping("/delete")
  public String deleteScheduleGet(RedirectAttributes redirectAttributes, HttpSession session) {
    log.info("------------ [Get schedule delete] ------------");
    Long clubNo = (Long) session.getAttribute("clubNo");

    //일정 삭제 후 세션에 저장된 일정번호 삭제
    Long scheduleNo = (Long) session.getAttribute("scheduleNo");
    scheduleService.deleteSchedule(scheduleNo);
    session.removeAttribute("scheduleNo");

    //결과 메세지
    redirectAttributes.addFlashAttribute("message", "일정이 삭제되었습니다.");

    return "redirect:/club/main/" + clubNo;
  }

}
