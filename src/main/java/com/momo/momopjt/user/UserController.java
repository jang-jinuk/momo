package com.momo.momopjt.user;


import com.momo.momopjt.report.ReportDTO;
import com.momo.momopjt.report.ReportService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/user/profile")
@Log4j2
public class UserController {
  //나의 신고 페이지
  @Autowired
  private ReportService reportService;

  //나의 신고 페이지
  @GetMapping("/my-reports")
  public String getMyReports(Model model, @RequestParam(defaultValue = "1") int page) {
    // 현재 인증된 사용자 정보 가져오기
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    User currentUser = (User) authentication.getPrincipal(); // User 객체로 캐스팅
    // 사용자가 신고한 내역 조회
    List<ReportDTO> myReports = reportService.readReport(currentUser);
    // 모델에 데이터 추가
    model.addAttribute("reportDTO", myReports);
//    int totalReports = myReports.size(); // 총 데이터 수
//    int pageSize = 10; // 한 번에 표시할 페이지 수
//    int lastPage = (totalReports + pageSize - 1) / pageSize; // 총 페이지 수
//
//    // 페이지에 맞게 데이터 나누기
//    int fromIndex = (page - 1) * pageSize;
//    int toIndex = Math.min(fromIndex + pageSize, totalReports);
//    List<ReportDTO> reports = myReports.subList(fromIndex, toIndex); // 현재 페이지의 신고 내역
//
//    // 페이지 그룹 계산
//    int pageGroupSize = 10; // 한 번에 표시할 페이지 번호 수
//    int currentGroup = (page - 1) / pageGroupSize;
//    int startPage = currentGroup * pageGroupSize + 1;
//    int endPage = Math.min(startPage + pageGroupSize - 1, lastPage);
//
//    // 모델에 데이터 추가
//    model.addAttribute("reportDTO", reports); // 현재 페이지의 신고 내역
//    model.addAttribute("totalReports", totalReports); // 총 신고 내역 수
//    model.addAttribute("page", page); // 현재 페이지 번호
//    model.addAttribute("lastPage", lastPage); // 총 페이지 수
//    model.addAttribute("startPage", startPage); // 시작 페이지 번호
//    model.addAttribute("endPage", endPage); // 끝 페이지 번호

    log.info("...... [get my-reports]..........KSW");
    return "/user/profile/my-report";  // 뷰 반환
    }
  //내가 신고한 내역 삭제
  @PostMapping("/delete")
  public String delete(@RequestParam("reportNo") Long reportNo) {
    log.info("...... [post delete report]..........KSW");
    reportService.removeReportHistory(reportNo);
    return "redirect:/user/profile/my-report";
    }
  }
