package com.momo.momopjt.report;

import com.momo.momopjt.user.User;
import com.momo.momopjt.user.UserRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

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
  //모달창으로 바뀌어서 사라짐

  //TODO 신고하기 컨트롤러
  @PostMapping("/create")
  public String reportUser(@RequestParam Long reportedUserId, ReportDTO reportDTO, HttpServletRequest request) {
    log.info("...... [ReportController/reportCreate/running]..........KSW");
    reportDTO.setReportResult('0');
    reportDTO.setReportDate(Instant.now());

    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
     String userNameTest = authentication.getName();

    User reporter = userRepository.findByUserId(userNameTest); // 로그인한 유저
    User reported = userRepository.findById(reportedUserId).orElseThrow(); // 신고할 유저

    reportDTO.setReporterNo(reporter);
    reportDTO.setReportedNo(reported);

    log.info("...... [{}]..........KSW", reportDTO.getReportCategory());
    reportService.addReport(reportDTO);

    return "redirect:" + request.getHeader("Referer"); // 이전 페이지 리디렉션
  } else {
    throw new RuntimeException("당신 누구야.");
      }
    }
  }

