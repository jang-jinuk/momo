package com.momo.momopjt.report;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin")
@Log4j2
public class ReportController {
  @Autowired
  private ReportService reportService;

  //조회와 페이징 검색
  @GetMapping("/manage-report")
  public String manageReport(Model model,
                             @RequestParam(value = "page", defaultValue = "1") int page,
                             @RequestParam(value = "query", defaultValue = "") String query) {
    log.info("...... [ReportController/manage-report/running]..........KSW");
    // 검색어를 사용하여 리포트 조회
    List<ReportDTO> sandReport;

    if (query.isEmpty()) {
      sandReport = reportService.readAllReport(); // 검색어가 비었으면 모든 리포트 조회
    } else {
      sandReport = reportService.searchReports(query); // 검색어에 따른 리포트 조회
    }

    int lastPage = (sandReport.size() + 9) / 10; // 페이지 갯수 설정 함수
    // 10개씩 리스트로 만들기
    if (page < 1 || page > lastPage) {
      page = 1; // 페이지가 유효하지 않은 경우 1페이지로 설정
    }
    List<ReportDTO> pageList = sandReport.stream()
        .skip(10L * (page - 1)) // 처음 (page-1) * 10개를 건너뜀
        .limit(10) // 다음 10개를 선택
        .collect(Collectors.toList());
    model.addAttribute("reportDTO", pageList);
    model.addAttribute("page", page);
    model.addAttribute("lastPage", lastPage);
    model.addAttribute("query", query); // 검색어를 모델에 추가
    return "/admin/manage-report";
  }
  //제제
  @PostMapping("/ban")
  public String ban(@RequestParam("reportNo") Long reportNo) {
    log.info("...... [get manage/justice]..........KSW");
    ReportDTO reportDTO = new ReportDTO(); // 타입에 맞게 객체를 생성하여
    reportDTO.setReportNo(reportNo); //담아주고
    // reportService.updateReport() 메서드 호출
    reportService.userBanReport(reportDTO); //기능으로 넘겨준다
    log.info("...... [정의구현]..........KSW");
    return "redirect:/admin/manage-report";
  }
  //제제해제
  @PostMapping("/freedom")
  public String freedom(@RequestParam("reportNo") Long reportNo) {
    log.info("...... [get manage/freedom]..........KSW");
    ReportDTO reportDTO = new ReportDTO(); // 타입에 맞게 객체를 생성하여
    reportDTO.setReportNo(reportNo); //담아주고
    // reportService.updateReport() 메서드 호출
    reportService.safeReport(reportDTO); //기능으로 넘겨준다
    log.info("...... [공소시효 만료]..........KSW");
    return "redirect:/admin/manage-report";
  }
  //삭제
  @PostMapping("/delete")
  public String delete(@RequestParam("reportNo") Long reportNo) {
    log.info("...... [post delete report]..........KSW");
    reportService.deleteReport(reportNo);
    return "redirect:/admin/manage-report";
  }
  //신고관리 페이징
 /* @GetMapping("/list")
  public String list(Model model, @RequestParam(value = "page", defaultValue = "0") int page){
    log.info("...... [Reportcontroller/list/running]..........KSW");
    Page<ReportDTO> paging = reportService.getReportList(page);
    model.addAttribute("paging", paging);
    return "redirect:/admin/manage-report";
  }*/
}
