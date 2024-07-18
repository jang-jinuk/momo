package com.momo.momopjt.report;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

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

    //페이지 수 설정
    int totalReports = sandReport.size(); // 총 데이터 수
    int pageSize = 10; // 한 번에 표시할 페이지 수
    int lastPage = (totalReports + pageSize - 1) / pageSize; // 총 페이지 수

    // 페이지에 맞게 데이터 나누기
    int fromIndex = (page - 1) * pageSize;
    int toIndex = Math.min(fromIndex + pageSize, totalReports);
    List<ReportDTO> reports = sandReport.subList(fromIndex, toIndex);

    // 페이지 그룹 계산
    int pageGroupSize = 10; // 한 번에 표시할 페이지 번호 수
    int currentGroup = (page - 1) / pageGroupSize;
    int startPage = currentGroup * pageGroupSize + 1;
    int endPage = Math.min(startPage + pageGroupSize - 1, lastPage);

    model.addAttribute("reportDTO", reports);
    model.addAttribute("page", page);
    model.addAttribute("lastPage", lastPage);
    model.addAttribute("query", query); // 검색어를 모델에 추가
    model.addAttribute("startPage", startPage);
    model.addAttribute("endPage", endPage);
    return "/admin/manage-report";
  }

  //제제
  @PostMapping("/ban")
  public String ban(@RequestParam("reportNo") Long reportNo,
                    @RequestParam("currentPage") int currentPage,
                    @RequestParam("query") String query) throws UnsupportedEncodingException {
    log.info("...... [get manage/justice]..........KSW");
    ReportDTO reportDTO = new ReportDTO(); // 타입에 맞게 객체를 생성하여
    reportDTO.setReportNo(reportNo); //담아주고
    // reportService.updateReport() 메서드 호출
    reportService.userBanReport(reportDTO); //기능으로 넘겨준다
    log.info("...... [정의구현]..........KSW");
    log.info("...... [{}]..........KSW",query);
    String encodedQuery = URLEncoder.encode(query, StandardCharsets.UTF_8); //쿼리를 인코더에 담아준다
    return "redirect:/admin/manage-report?page=" + currentPage + "&query=" + encodedQuery;
  }

  //제제해제
  @PostMapping("/freedom")
  public String freedom(@RequestParam("reportNo") Long reportNo,
                        @RequestParam("currentPage") int currentPage,
                        @RequestParam("query") String query) throws UnsupportedEncodingException {
    log.info("...... [get manage/freedom]..........KSW");
    ReportDTO reportDTO = new ReportDTO(); // 타입에 맞게 객체를 생성하여
    reportDTO.setReportNo(reportNo); //담아주고
    // reportService.updateReport() 메서드 호출
    reportService.safeReport(reportDTO); //기능으로 넘겨준다
    log.info("...... [공소시효 만료]..........KSW");
    String encodedQuery = URLEncoder.encode(query, "UTF-8");
    return "redirect:/admin/manage-report?page=" + currentPage + "&query=" + encodedQuery;
  }

  //삭제
  @PostMapping("/delete")
  public String delete(@RequestParam("reportNo") Long reportNo,
                       @RequestParam("currentPage") int currentPage,
                       @RequestParam("query") String query) throws UnsupportedEncodingException{
    log.info("...... [post delete report]..........KSW");
    reportService.deleteReport(reportNo);
    String encodedQuery = URLEncoder.encode(query, StandardCharsets.UTF_8);
    return "redirect:/admin/manage-report?page=" + currentPage + "&query=" + encodedQuery;
  }
}
