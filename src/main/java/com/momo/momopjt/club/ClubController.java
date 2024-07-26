package com.momo.momopjt.club;

import com.momo.momopjt.photo.Photo;
import com.momo.momopjt.article.ArticleDTO;
import com.momo.momopjt.article.ArticleService;
import com.momo.momopjt.photo.PhotoDTO;
import com.momo.momopjt.photo.PhotoRepository;
import com.momo.momopjt.photo.PhotoService;
import com.momo.momopjt.schedule.ScheduleDTO;
import com.momo.momopjt.schedule.ScheduleService;
import com.momo.momopjt.user.User;
import com.momo.momopjt.user.UserService;
import com.momo.momopjt.userandclub.UserAndClubDTO;
import com.momo.momopjt.userandclub.UserAndClubRepository;
import com.momo.momopjt.userandclub.UserAndClubService;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
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
  private UserAndClubService userAndClubService;
  @Autowired
  private PhotoService photoService;
  @Autowired
  private PhotoRepository photoRepository;
  @Autowired
  private ArticleService articleService;
  @Autowired
  private ModelMapper modelMapper;
  @Autowired
  private UserAndClubRepository userAndClubRepository;

  //모임 메인페이지 조회
  @GetMapping("/main/{clubNo}")
  public String readClubGet(@PathVariable("clubNo") Long clubNo, Model model, HttpSession session) {
    log.info("------------ [Get club main no: {}] ------------", clubNo);

    ClubDTO clubDTO = clubService.readOneClub(clubNo);
    model.addAttribute("club", clubDTO);
    Club club = new Club();
    club.setClubNo(clubNo);
    List<ScheduleDTO> endSchedules = scheduleService.readEndSchedules(club); //마감된 일정
    model.addAttribute("endSchedules", endSchedules);
    List<ScheduleDTO> getOngoingSchedules = scheduleService.readOngoingSchedules(club);//마감되지 않은 일정
    model.addAttribute("getOngoingSchedules", getOngoingSchedules);
    log.info("------------ [found schedules] ------------");

    List<ArticleDTO> articles = articleService.getAllArticlesByClub(club); //후기 글
    model.addAttribute("articles", articles);

    //0722 YY
//    //scheduleDTOList 에 담긴 사진 확인 로그
//    for(ScheduleDTO s : scheduleDTOList) {
//      log.trace(s.getSchedulePhoto());
//    }

    session.setAttribute("clubNo", clubDTO.getClubNo());
    //세션에 모임 clubNo을 저장하고 해당 모임 일정 및 게시글 처리시 사용

    //TODO 현재 로그인한 회원 정보 조회하는 로직 메서드로 따로 분리할 건지 생각해보기 JW
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    String username = auth.getName();
    User user = userService.findByUserId(username);
    UserAndClubDTO userAndClubDTO = new UserAndClubDTO();
    userAndClubDTO.setUserNo(user);
    userAndClubDTO.setClubNo(club);
    int isMember = userAndClubService.isMember(userAndClubDTO); //모임에 소속되었는지 확인
    if (isMember == 0 || isMember == 1) {
      return "redirect:/club/join-page";
    }
    model.addAttribute("isMember", isMember);

    // 유저의 찜 상태 조회
    //UserAndClubDTO clubNos = userAndClubService.
    //char isWish = userAndClubService.readWishClubStatus(user, club);
    // model.addAttribute("isWish", isWish); // 모델에 찜 상태 추가

    //YY
    //일정 사진 표시 기능
//    for (ScheduleDTO scheduleDTO : scheduleDTOList) {
//      String schedulePhoto64 = photoService.getPhoto64(scheduleDTO.getSchedulePhoto());
//      String[] list = new String[0];
//      Arrays.stream(list).map(schedulePhoto64);
//      model.addAttribute("schdule64List", schedule64List);
//    }
//List<String> schedule64List = new ArrayList<>();
//
//for (ScheduleDTO scheduleDTO : scheduleDTOList) {
//    String schedulePhoto64 = photoService.getPhoto64(scheduleDTO.getSchedulePhoto());
//    schedule64List.add(schedulePhoto64);
//    log.trace(schedulePhoto64.substring(1,100)+"---------------------------------------------------------");
//}
//model.addAttribute("schedule64List", schedule64List);
//    log.info("----------------- [07-18 11:43:22]-----------------");
//log.info(schedule64List.get(0).substring(1,100));
//log.info(schedule64List.get(1).substring(1,100));

    //YY
    // 사용자 즐겨찾기 클럽 목록을 가져옵니다.
    List<Club> wishLists = userAndClubService.findMyWishClubs(user);
    log.info("User 의 즐겨찾기 클럽 목록: {}", wishLists);

    // clubNo가 wishLists 에 있는지 확인
    boolean isWishInList = wishLists.stream()
        .anyMatch(clubItem -> clubItem.getClubNo().equals(clubNo));
    log.info("클럽 번호 {}가 즐겨찾기 목록에 포함되어 있는지 여부: {}", clubNo, isWishInList);

    // isWish 상태를 설정
    char isWish = isWishInList ? 'Y' : 'N';
    model.addAttribute("isWish", isWish);
    log.info("모델에 추가된 isWish 값: {}", isWish);

    return "club/main";
  }


  @GetMapping("/create")
  public String createClubGet() {
    log.info("------------ [Get club create] ------------");
    return "club/create";
  }

  //모임 생성
  @PostMapping("/create")
  public String createClubPost(ClubDTO clubDTO, PhotoDTO photoDTO, RedirectAttributes redirectAttributes) {
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
      clubNo = clubService.createClub(clubDTO, photoDTO, userAndClubDTO);

    } catch (ClubService.ClubNameException e) {
      redirectAttributes.addFlashAttribute("error", "clubName");
      return "redirect:/club/create";
    }

    redirectAttributes.addFlashAttribute("message", "축하합니다! 모임이 생성되었습니다!");

    return "redirect:/club/main/" + clubNo;
  }

  @GetMapping("/update")
  public String updateClubGet(Model model, HttpSession session) {
    log.info("------------ [Get club update] ------------");
    Long clubNo = (Long) session.getAttribute("clubNo");
    ClubDTO clubDTO = clubService.readOneClub(clubNo);
    model.addAttribute("clubDTO", clubDTO);
    return "club/update";
  }

  //모임 수정
  @PostMapping("/update")
  public String updateClubPost(ClubDTO clubDTO, PhotoDTO photoDTO, RedirectAttributes redirectAttributes, HttpSession session) {
    log.info("------------ [Post club update] ------------");
    Long clubNo = (Long) session.getAttribute("clubNo");
    clubDTO.setClubNo(clubNo);

    Boolean isSuccess = clubService.updateClub(clubDTO, photoDTO);

    if (isSuccess) {
      redirectAttributes.addFlashAttribute("message", "모임 수정이 완료되었습니다.");
      return "redirect:/club/main/" + clubNo;
    }
    redirectAttributes.addFlashAttribute("message", "모임 구성원 수보다 적게 수정할 수 없습니다.");
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
    redirectAttributes.addFlashAttribute("message", "모임이 해산되었습니다.");
    return "redirect:/home";
  }

  @GetMapping("/leave-page")
  public String goLeavePageGet() {
    log.info("------------ [Get club leave-page] ------------");
    return "club/leave";
  }

  //모임 탈퇴
  @GetMapping("/leave")
  public String leaveClubGet(@RequestParam(value = "userNo", required = false) Long userNo, HttpSession session, RedirectAttributes redirectAttributes) {
    log.info("------------ [Get club leave] ------------");
    Long clubNo = (Long) session.getAttribute("clubNo");
    Club club = new Club();
    club.setClubNo(clubNo);

    User user = new User();
    user.setUserNo(userNo);

    UserAndClubDTO userAndClubDTO = new UserAndClubDTO();
    userAndClubDTO.setClubNo(club);

    if (userNo != null) { //모임장이 모임원을 탈퇴 시킬 때
      userAndClubDTO.setUserNo(user);
      userAndClubService.leaveClub(userAndClubDTO);
      redirectAttributes.addFlashAttribute("message", "모임원이 삭제 되었습니다.");
      return "redirect:/club/members";
    }

    //TODO 현재 로그인한 회원 정보 조회하는 로직 메서드로 따로 분리할 건지 생각해보기 JW
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    String username = auth.getName();
    user = userService.findByUserId(username);
    userAndClubDTO.setUserNo(user);

    userAndClubService.leaveClub(userAndClubDTO);

    session.removeAttribute("clubNo");

    redirectAttributes.addFlashAttribute("message", "모임에서 정상적으로 탈퇴되었습니다.");

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

    UserAndClubDTO userAndClubDTO = new UserAndClubDTO();
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
    ClubDTO clubDTO = clubService.readOneClub(clubNo);
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
  public String approveJoinGet(@RequestParam("userNo") Long userNo, HttpSession session,
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
    if (!isSuccess) {
      redirectAttributes.addFlashAttribute("message", "인원 초과입니다. 최대인원수를 수정해주세요");
      return "redirect:/club/members";
    }

    redirectAttributes.addFlashAttribute("message", "승인이 완료되었습니다.");
    return "redirect:/club/members";
  }

  @PostMapping("/update-Wish")
  public String updateWishClub(@RequestParam("clubNo") Long clubNo,
                               @RequestParam("isWish") char isWish) {
    log.info("------------ [클럽 번호: {}에 대한 즐겨찾기 업데이트] ------------", clubNo);

    // 현재 로그인한 사용자 정보 조회
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    String username = auth.getName();
    log.info("현재 로그인한 사용자: {}", username);

    User user = userService.findByUserId(username);
    log.info("조회된 사용자 정보: {}", user);

    ClubDTO wishClub = clubService.readOneClub(clubNo);
    log.info("조회된 클럽 정보: {}", wishClub);

    Club ohClub = modelMapper.map(wishClub, Club.class);
    log.info("ClubDTO에서 Club으로 변환된 결과: {}", ohClub);

    // UserAndClubDTO 객체 생성 및 값 설정
    UserAndClubDTO userAndClubDTO = new UserAndClubDTO();
    userAndClubDTO.setUserNo(user);
    userAndClubDTO.setClubNo(ohClub);
    log.info("생성된 UserAndClubDTO: {}", userAndClubDTO);

    // isWish 상태 업데이트 (반전 없이 그대로 사용)
    userAndClubDTO.setIsWish(isWish);
    log.info("업데이트할 isWish 상태: {}", userAndClubDTO.getIsWish());

    // 즐겨찾기 상태 업데이트
    userAndClubService.updateWishClub(userAndClubDTO);
    log.info("데이터베이스에 업데이트된 즐겨찾기 상태: {}", userAndClubDTO);

    // 클럽 메인 페이지로 리다이렉트
    log.info("클럽 번호 {}에 대한 메인 페이지로 리다이렉트", clubNo);
    return "redirect:/club/main/" + clubNo;
  }

}
