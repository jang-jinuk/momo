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
  public String report(Model model) {
    log.info("...... [get profile/my-report]..........KSW");
    // ID를 조회하여 모델에 추가 (임시)
    User user = new User();
    //SecurityContextHolder.getContext().getAuthentication().getPrincipal(); //로그인 정보 가져옴
    user.setUserNo(4L); //TODO 로그인된 사용자 정보 불러와서 아이디 넣어줘야 함 SW
    List<ReportDTO> sandIdReport = reportService.readReport(user);
    model.addAttribute("reportDTO", sandIdReport);
    log.info("...... []..........KSW");
    return "/user/profile/my-report";  // 뷰 반환
  }
  //내가 신고한 내역 삭제 (아직 미사용)
//  @PostMapping("/delete")
//  public String delete(@RequestParam("reportNo") Long reportNo) {
//    log.info("...... [post delete report]..........KSW");
//    reportService.removeReportHistory(reportNo);
//    return "redirect:/user/profile/my-report";
//  }
}