package com.momo.momopjt.user;


import com.momo.momopjt.report.ReportDTO;
import com.momo.momopjt.report.ReportService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/user/profile")
@Log4j2
public class UserController {
  //나의 신고 페이지
  @Autowired
  private ReportService reportService;
  //나의 신고 페이지
  @GetMapping("/my-report")
  public String report(Model model) {
    log.info("...... [get profile/my-report]..........KSW");
    // 모든 리포트를 조회하여 모델에 추가
    List<ReportDTO> sandReport = reportService.readAllReport();
    model.addAttribute("reportDTO", sandReport);
    return "/user/profile/my-report";  // 뷰 반환
  }
}
