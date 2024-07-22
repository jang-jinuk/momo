package com.momo.momopjt.report;

import com.momo.momopjt.user.User;
import com.momo.momopjt.user.UserRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
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
  public String reportUser(ReportDTO reportDTO, HttpServletRequest request) {
    log.info("...... [report /create POST ]..........KSW");
    // 서비스로 데이터 전달
    reportDTO.setReportNo(-1L);
    reportDTO.setReportResult('0');
    reportDTO.setReportDate(Instant.now());
    //User 객체 만들어줌
    User user1 = userRepository.findById(1L).orElseThrow();
    User user2 = userRepository.findById(2L).orElseThrow();

    reportDTO.setReporterNo(user1); // 로그인 한 유저 정보가 들어와야함
    reportDTO.setReportedNo(user2); // 신고하기 눌러서 들어올때 값 전달받아서 와야함.
    log.info("...... [{}]..........KSW", reportDTO.getReportCategory());
    reportService.addReport(reportDTO);
    return "redirect:" + request.getHeader("Referer"); // 이전 페이지 리디렉션 (현재 신고 생성 창으로 가짐)
  }

//  // 신고 정보 설정
//    reportDTO.setReportNo(-1L);
//    reportDTO.setReportResult('0');
//    reportDTO.setReportDate(Instant.now());
//
//  // 로그인 사용자 정보를 가져오기 (예: SecurityContextHolder에서 가져오기)
//  User reporter = userRepository.findById(1L).orElseThrow(); // 실제 로그인된 사용자 ID로 대체 필요
//
//  // 신고 대상 사용자 정보를 가져오기
//  Long reportedNo = Long.valueOf(request.getParameter("reportedNo"));
//  User reported = userRepository.findById(reportedNo).orElseThrow();
//
//    reportDTO.setReporterNo(reporter);
//    reportDTO.setReportedNo(reported);
//
//    log.info("...... [{}]..........KSW", reportDTO.getReportCategory());
//    reportService.addReport(reportDTO);
//
//    return ResponseEntity.ok().body("Report successfully submitted."); // 이전 페이지로 리디렉션
//}
}

