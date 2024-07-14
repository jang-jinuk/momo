package com.momo.momopjt.report;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin")
@Log4j2
public class ReportController {
  @Autowired
  private ReportService reportService;

  @GetMapping("/manage-report")
  public String report(Model model) {
    log.info("...... [get manage/report]..........KSW");
    // 모든 리포트를 조회하여 모델에 추가
    List<ReportDTO> sandReport = reportService.readAllReport();
    model.addAttribute("reportDTO", sandReport);
    return "/admin/manage-report";  // 뷰 반환
  }
  @PostMapping("/justice")
  public String justice(@RequestParam("reportNo") Long reportNo) {
    log.info("...... [get manage/justice]..........KSW");
    ReportDTO reportDTO = new ReportDTO(); // 타입에 맞게 객체를 생성하여
    reportDTO.setReportNo(reportNo); //담아주고
    // reportService.updateReport() 메서드 호출
    reportService.updateReport(reportDTO); //기능으로 넘겨준다
    log.info("...... [정의구현]..........KSW");
    return "redirect:/admin/manage-report";
  }

  @PostMapping("/delete")
  public String delete(@RequestParam("reportNo") Long reportNo) {
    log.info("...... [post delete report]..........KSW");
    reportService.deleteReport(reportNo);
    return "redirect:/admin/manage-report";
  }
}
