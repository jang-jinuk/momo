package com.momo.momopjt.report;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/admin")
@Log4j2
public class ReportController {
  @Autowired
  private ReportService reportService;

  @GetMapping("/manage-report")
  public String report(Model model) {
    // 모든 리포트를 조회하여 모델에 추가
    List<ReportDTO> sandReport = reportService.readAllReport();
    model.addAttribute("reportDTOs", sandReport);
    return "/admin/manage-report";  // 뷰 반환
  }
}
