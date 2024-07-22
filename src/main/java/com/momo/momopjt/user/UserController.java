package com.momo.momopjt.user;


import com.momo.momopjt.report.ReportDTO;
import com.momo.momopjt.report.ReportService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
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
  @GetMapping("/my-report")
  public String report(Model model){
                       //@RequestParam(value = "page", defaultValue = "1") int page) {
    log.info("...... [get profile/my-report]..........KSW");
    // ID를 조회하여 모델에 추가 (임시)
    User user = new User();
    //SecurityContextHolder.getContext().getAuthentication().getPrincipal(); //로그인 정보 가져옴
    user.setUserNo(4L);
    //TODO 로그인된 사용자 정보 불러와서 아이디 넣어줘야 함 그 후 페이징 처리 SW
    List<ReportDTO> sandIdReport = reportService.readReport(user);

   // int totalReports = sandIdReport.size(); // 총 데이터 수
   // int pageSize = 10; // 한 번에 표시할 페이지 수
   // int lastPage = (totalReports + pageSize - 1) / pageSize; // 총 페이지 수

    // 페이지에 맞게 데이터 나누기
//    int fromIndex = (page - 1) * pageSize;
//    int toIndex = Math.min(fromIndex + pageSize, totalReports);
//    List<ReportDTO> reports = sandIdReport.subList(fromIndex, toIndex);

    // 페이지 그룹 계산
//    int pageGroupSize = 10; // 한 번에 표시할 페이지 번호 수
//    int currentGroup = (page - 1) / pageGroupSize;
//    int startPage = currentGroup * pageGroupSize + 1;
//    int endPage = Math.min(startPage + pageGroupSize - 1, lastPage);
    model.addAttribute("reportDTO", sandIdReport);
//    model.addAttribute("page", page);
//    model.addAttribute("lastPage", lastPage);
//    model.addAttribute("startPage", startPage);
//    model.addAttribute("endPage", endPage);

    log.info("...... [userIdReport]..........KSW");
    return "/user/profile/my-report";  // 뷰 반환
  }
//  //내가 신고한 내역 삭제
//  @PostMapping("/delete")
//  public String delete(@RequestParam("reportNo") Long reportNo) {
//    log.info("...... [post delete report]..........KSW");
//    reportService.deleteReport(reportNo);
//    return "redirect:/user/profile/my-report";
//  }
}
