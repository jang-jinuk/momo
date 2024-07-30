package com.momo.momopjt.club;

import com.momo.momopjt.article.ArticleDTO;
import com.momo.momopjt.article.ArticleService;
import com.momo.momopjt.photo.Photo;
import com.momo.momopjt.photo.PhotoRepository;
import com.momo.momopjt.photo.PhotoService;
import com.momo.momopjt.schedule.ScheduleDTO;
import com.momo.momopjt.schedule.ScheduleService;
import com.momo.momopjt.user.User;
import com.momo.momopjt.user.UserService;
import com.momo.momopjt.userandclub.UserAndClubDTO;
import com.momo.momopjt.userandclub.UserAndClubService;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


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
  private PhotoService photoService;
  @Autowired
  private ArticleService articleService;

  @Autowired
  private UserAndClubService userAndClubService;

  @Autowired
  private PhotoRepository photoRepository;

//  @Qualifier("modelMapper")
  @Autowired
  private ModelMapper modelMapper;

  //모임 메인페이지 조회
  @GetMapping("/main/{clubNo}")
  public String readClubGet(@PathVariable("clubNo") Long clubNo, Model model, HttpSession session) {
    log.info("------------ [Get club main no: {}] ------------",clubNo);
    
    ClubDTO clubDTO = clubService.readOneClub(clubNo);
    model.addAttribute("clubDTO", clubDTO);
    log.info("----------------- [clubDTO : {}]-----------------",clubDTO);

    Club club = new Club();
    club.setClubNo(clubNo);


    List<ScheduleDTO> endSchedules = scheduleService.readEndSchedules(club); //마감된 일정
    model.addAttribute("endSchedules", endSchedules);

    List<ScheduleDTO> getOngoingSchedules= scheduleService.readOngoingSchedules(club);//마감되지 않은 일정
    model.addAttribute("getOngoingSchedules", getOngoingSchedules);
    log.info("------------ [found schedules] ------------");

    List<String> endSchedulePhotoList = new ArrayList<>();
    //일정 끝난 사진 파일들 조회
    if(!endSchedules.isEmpty()) {
      endSchedulePhotoList = endSchedules.stream()
          .map(scheduleDTO -> photoService.getPhoto(scheduleDTO.getSchedulePhotoUUID()).toString())
          .collect(Collectors.toList());
    }
    //일정 끝난 사진 파일들 view로 데이터 넘김
    model.addAttribute("endSchedulePhotoList", endSchedulePhotoList);
    log.trace("----------------- [endSchedulePhotoList : {}]-----------------",endSchedulePhotoList);

    //일정 진행중 사진 파일들 조회
    List<String> ongoingSchedulePhotoList = new ArrayList<>();
    if(!getOngoingSchedules.isEmpty()){
      ongoingSchedulePhotoList = getOngoingSchedules.stream()
          .map(scheduleDTO -> photoService.getPhoto(scheduleDTO.getSchedulePhotoUUID()).toString())
          .collect(Collectors.toList());
    }
    //일정 진행중 사진 파일들 view로 데이터 넘김
    model.addAttribute("ongoingSchedulePhotoList", ongoingSchedulePhotoList);
    log.trace("----------------- [ongoingSchedulePhotoList : {}]-----------------",ongoingSchedulePhotoList);


    log.info("----------------- [club photo 처리]-----------------");
    log.trace("----------------- [club photo uuid : {}]-----------------",clubDTO.getClubPhotoUUID());
    Photo clubPhoto = photoService.getPhoto(clubDTO.getClubPhotoUUID());
    String clubProfilePhotoStr = clubPhoto.toString();
    log.trace("----------------- [clubPhoto str 결과 : {}]-----------------",clubProfilePhotoStr);
    model.addAttribute("clubProfilePhoto",clubProfilePhotoStr);
    // 이미지 html 에서 쓸때 아래 처럼 (예시)
    // <img th:src="@{/{fileName}(fileName=${clubProfilePhoto})}" alt="club profile image">




    log.info("----------------- [clubphoto str {}]-----------------",clubProfilePhotoStr);



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


    List<ArticleDTO> articles = articleService.getAllArticles(club); //후기 글 조회
    model.addAttribute("articles", articles);
    List<String> articlePhotoList = new ArrayList<>();
    //YY 후기글 이미지 처리
    if(!articles.isEmpty()) {
      articlePhotoList = articles.stream()
          .map(articleDTO -> photoService.getPhoto(articleDTO.getArticlePhotoUUID()).toString())//YY
          .collect(Collectors.toList());
    }
    //일정 끝난 사진 파일들 view로 데이터 넘김
    model.addAttribute("articlePhotoList", articlePhotoList);
    log.trace("----------------- [endSchedulePhotoList : {}]-----------------", articlePhotoList);



    return "club/main";
  }
  @GetMapping("/create")
  public String createClubGet() {
    log.info("------------ [Get club create] ------------");
    return "club/create";
  }

  //모임 생성
  @PostMapping("/create")
  public String createClubPost(ClubDTO clubDTO, RedirectAttributes redirectAttributes)  { // 0729 YY photoDTO 제거
    log.info("------------ [Post club create] ------------");

    //TODO 현재 로그인한 회원 정보 조회하는 로직 메서드로 따로 분리할 건지 생각해보기 JW
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    String username = auth.getName();
    User user = userService.findByUserId(username);
    UserAndClubDTO userAndClubDTO = new UserAndClubDTO();
    userAndClubDTO.setUserNo(user);
    userAndClubDTO.setJoinDate(Instant.now());
    Long clubNo;
    //TODO 파일 업로드 기능과 연결필요 JW

    try {
      clubNo = clubService.createClub(clubDTO, userAndClubDTO); //0729 YY photoDTO 따로 필요 x

    } catch (ClubService.ClubNameException e) {
      redirectAttributes.addFlashAttribute("error", "clubName");
      return "redirect:/club/create";
    }

    redirectAttributes.addFlashAttribute("message", "축하합니다! 모임이 생성되었습니다!");

    return "redirect:/club/main/" + clubNo;
  }

  @GetMapping("/update/{clubNo}")
  public String updateClubGet(@PathVariable("clubNo") Long clubNo, Model model, HttpSession session) {
    log.info("------------ [Get club update] ------------");
    clubNo = (Long) session.getAttribute("clubNo");
    ClubDTO clubDTO = clubService.readOneClub(clubNo);
    model.addAttribute("clubDTO", clubDTO);

    //모임대표사진 출력
    log.trace(clubDTO.getClubPhotoUUID());
    log.trace(photoService.getPhoto(clubDTO.getClubPhotoUUID().toString()));
    String clubProfilePhoto = photoService.getPhoto(clubDTO.getClubPhotoUUID()).toString();

    model.addAttribute("clubProfilePhoto", clubProfilePhoto);
    return "club/update";
  }

  //모임 수정
  @PostMapping("/update")
  public String updateClubPost(ClubDTO clubDTO, RedirectAttributes redirectAttributes, HttpSession session) {
    log.info("------------ [Post club update] ------------");
    Long clubNo = (Long) session.getAttribute("clubNo");
    log.trace("clubNo : {}",clubNo);

    clubDTO.setClubNo(clubNo);

    Boolean isSuccess = clubService.updateClub(clubDTO);

    if (isSuccess) {
      redirectAttributes.addFlashAttribute("message","모임 수정이 완료되었습니다.");
      return "redirect:/club/main/" + clubNo;
    }
    redirectAttributes.addFlashAttribute("message","모임 구성원 수보다 적게 수정할 수 없습니다.");
    return "redirect:/club/update";
  }

  @GetMapping("/disband-page")
  public String goDisbandPageGet() {
    log.info("------------ [Get club disband-page] ------------");
    return "club/disband";
  }

  //모임 해산
  @GetMapping("/disband")
  public String disbandClubGet(HttpSession session, RedirectAttributes redirectAttributes) {
    log.info("------------ [Get club disband] ------------");
    Long clubNo = (Long) session.getAttribute("clubNo");
    clubService.deleteClub(clubNo);
    session.removeAttribute("clubNo");
    redirectAttributes.addFlashAttribute("message","모임이 해산되었습니다.");
    return "redirect:/home";
  }

  @GetMapping("/leave-page")
  public String goLeavePageGet() {
    log.info("------------ [Get club leave-page] ------------");
    return "club/leave";
  }

  //모임 탈퇴
  @GetMapping("/leave")
  public String leaveClubGet(@RequestParam(value = "userNo", required = false)Long userNo, HttpSession session, RedirectAttributes redirectAttributes) {
    log.info("------------ [Get club leave] ------------");
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

    return "redirect:/home";
  }

  @GetMapping("/join-page")
  public String goJoinPageGet(HttpSession session, Model model) {
    log.info("------------ [Get club join-page] ------------");
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

    UserAndClubDTO isLeader = userAndClubService.findLeader(club);
    ClubDTO clubDTO = clubService.readOneClub(clubNo);
    List<UserAndClubDTO> userAndClubDTOS = userAndClubService.readAllMembers(club);
    int countMembers = userAndClubService.countMembers(club);

    model.addAttribute("isLeader", isLeader); //모임장 정보
    model.addAttribute("clubDTO", clubDTO); //모임 정보
    model.addAttribute("userAndClubDTOS", userAndClubDTOS); // 모임 맴버 정보
    model.addAttribute("countMembers", countMembers);//모임 맴버 인원 수
    model.addAttribute("isMember", isMember); //가입 신청 여부
    return "club/join";
  }

  //모임 가입 신청
  @GetMapping("/join")
  public String joinClubGet(HttpSession session) {
    log.info("------------ [Get club join] ------------");
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
  public String joinClubCompleteGet(HttpSession session, Model model) {
    log.info("------------ [Get club Join complete] ------------");
    Long clubNo = (Long) session.getAttribute("clubNo");
    ClubDTO clubDTO= clubService.readOneClub(clubNo);
    model.addAttribute("clubDTO", clubDTO);
    return "club/join-complete";
  }

  //맴버 관리
  @GetMapping("/members")
  public String goMembersGet(Model model, HttpSession session) {
    log.info("------------ [Get club members] ------------");
    Long clubNo = (Long) session.getAttribute("clubNo");
    Club club = new Club();
    club.setClubNo(clubNo);
    List<UserAndClubDTO> userAndClubDTOS = userAndClubService.readAllMembers(club);
    List<UserAndClubDTO> joinList = userAndClubService.readAllJoinList(club);

    model.addAttribute("clubNo", clubNo);
    model.addAttribute("userAndClubDTOS", userAndClubDTOS);
    model.addAttribute("joinList", joinList);

    return "club/members";
  }

  //가입 신청 승인
  @GetMapping("/approve-join")
  public String approveJoinGet(@RequestParam("userNo")Long userNo, HttpSession session,
                            RedirectAttributes redirectAttributes) {
    log.info("------------ [Get club approve join] ------------");
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
