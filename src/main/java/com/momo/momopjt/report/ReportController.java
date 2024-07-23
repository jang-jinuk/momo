package com.momo.momopjt.report;

import com.momo.momopjt.user.User;
import com.momo.momopjt.user.UserRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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

    // 현재 로그인한 유저 정보 가져오기
    // 이 부분은 실제 로그인한 사용자의 ID를 가져오는 방법에 따라 변경될 수 있습니다.
      // 예시로 세션에서 가져온다고 가정합니다.
    //Long loggedInUserId = (Long) request.getSession().getAttribute("loggedInUserId"); // 세션에서 로그인한 사용자 ID 가져오기
    User reporter = userRepository.findById(2L).orElseThrow(); // 로그인한 유저
    User reportedUser = userRepository.findById(reportedUserId).orElseThrow(); // 신고할 유저

    reportDTO.setReporterNo(reporter);
    reportDTO.setReportedNo(reportedUser);

    log.info("...... [{}]..........KSW", reportDTO.getReportCategory());
    reportService.addReport(reportDTO);
    return "redirect:" + request.getHeader("Referer"); // 이전 페이지 리디렉션
  }
}

