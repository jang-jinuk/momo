package com.momo.momopjt.globalreport;

import com.momo.momopjt.report.Report;
import com.momo.momopjt.report.ReportDTO;
import com.momo.momopjt.report.ReportRepository;
import com.momo.momopjt.report.ReportService;
import com.momo.momopjt.user.User;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/global")
@Log4j2
//신고 버튼 누를시 나오고 신고하는 컨트롤러
public class GlobalReportController {
  @Autowired
  private ReportService reportService;

//  @GetMapping("/report"){
//    log.info("...... [report gogo]..........KSW");
//    // ID를 조회하여 신고내역에 추가 (임시)
//    reportService.addReport();
  }
