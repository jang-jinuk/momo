package com.momo.momopjt.report;

import com.momo.momopjt.club.Club;
import com.momo.momopjt.user.User;
import com.momo.momopjt.user.UserRepository;
import com.momo.momopjt.userandclub.UserAndClubDTO;
import com.momo.momopjt.userandclub.UserAndClubService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.time.Instant;

@Controller
@RequestMapping("/report")
@Log4j2
//신고 버튼 누를시 나오고 신고하는 컨트롤러
public class ReportController {

  @Autowired
  private ReportService reportService;
  @Autowired
  private UserRepository userRepository;
  @Autowired
  private UserAndClubService userAndClubService;

  //신고하기 페이지
  @GetMapping("/create")
  public String showReportPage(Model model) {
    log.info("...... [report /create Get]..........KSW");
    //TODO 넘어올 페이지에서 넣어줘야 함
    Long reportedNo = 1L;
    model.addAttribute("reportedNo", reportedNo);
    return "report/create";
  }

  //TODO 신고하기 컨트롤러
  @PostMapping("/create")
  public String reportUser(ReportDTO reportDTO, HttpServletRequest request, HttpSession session) {
    reportDTO.setReportResult('0');
    reportDTO.setReportDate(Instant.now());

    //User 객체 만들어줌
    User user1 = userRepository.findById(1L).orElseThrow();
    User user2 = userRepository.findById(2L).orElseThrow();

//    Long clubNo = (Long) session.getAttribute("clubNo"); //세션 String clubNo Long 타입으로 받아오기
//    Club club = new Club(); //클럽 객체 만들고
//    club.setClubNo(clubNo); //만든 객체에 clubNo 를 넣어준다
//    UserAndClubDTO userAndClubDTO = userAndClubService.isLeader(club); //메소드로 모임장 가져와서 DTO 에 넣기
//    User user2 = userAndClubDTO.getUserNo(); //DTO 에 담겨있는 모임장 번호 꺼내기

    reportDTO.setReporterNo(user1); // 로그인 한 유저 정보가 들어와야함
    reportDTO.setReportedNo(user2); // 신고하기 눌러서 들어올때 값 전달받아서 와야함.
    log.info("...... [{}]..........KSW", reportDTO.getReportCategory());
    reportService.addReport(reportDTO);
    return "redirect:" + request.getHeader("Referer"); // 이전 페이지 리디렉션 (현재 신고 생성 창으로 가짐)
  }
//
//  @PostMapping("/create")
//  public ResponseEntity<?> reportUser(@RequestParam String reportCategory, @RequestParam Long reportedNo) {
//    log.info("...... [report /create POST ]..........KSW");
//
//    // ReportDTO 객체 생성 및 설정
//    ReportDTO reportDTO = new ReportDTO();
//    reportDTO.setReportNo(-1L); // 기본 값
//    reportDTO.setReportResult('0'); // 기본 값
//    reportDTO.setReportDate(Instant.now()); // 현재 시간 설정

//    // 신고자와 신고 대상 사용자 정보 설정
//    User reporter = userRepository.findById(1L).orElseThrow(); // 로그인 한 사용자
//    User reported = userRepository.findById(reportedNo).orElseThrow(); // 신고 대상 사용자

//    reportDTO.setReporterNo(reporter);
//    reportDTO.setReportedNo(reported);
//    reportDTO.setReportCategory(reportCategory); // 신고 카테고리 설정

//    log.info("...... [{}]..........KSW", reportDTO.getReportCategory());
//
//    신고 정보를 서비스로 전달
//    reportService.addReport(reportDTO);
//
//    성공 응답 반환
//    return ResponseEntity.ok().body("신고가 완료되었습니다");
//  }
}

