package com.momo.momopjt.report;

import com.momo.momopjt.user.User;
import com.momo.momopjt.user.UserRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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

  //신고하기
  @PostMapping("/create")
  public String reportUser(@RequestParam Long reportedUserId, ReportDTO reportDTO, HttpServletRequest request, RedirectAttributes redirectAttributes) {
    log.info("...... [ReportController/reportCreate/running]..........KSW");
    reportDTO.setReportResult('0');
    reportDTO.setReportDate(Instant.now());

    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
      String userNameTest = authentication.getName();
      try {
        User reporter = userRepository.findByUserId(userNameTest); // 신고자 정보
        User reported = userRepository.findById(reportedUserId)
            .orElseThrow(() -> new RuntimeException("신고할 유저를 찾을 수 없습니다.")); // 신고할 유저 정보

        // 신고 카테고리 검증
        if (reportDTO.getReportCategory() == null || reportDTO.getReportCategory().isEmpty()) {
          throw new RuntimeException("신고 카테고리를 선택해주세요."); // 카테고리가 선택되지 않은 경우 예외 처리
        }
        reportDTO.setReporterNo(reporter); // 신고자 설정
        reportDTO.setReportedNo(reported); // 신고당한 유저 설정

        log.info("...... [{}]..........KSW", reportDTO.getReportCategory()); // 카테고리 로그 출력
        reportService.addReport(reportDTO); // 신고 저장

        return "redirect:/user/profile/testprofile"; // 성공 시 이동
      } catch (RuntimeException e) {
        log.info("...... [예외 처리 뭔가 걸림]..........KSW");
        redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        return "redirect:/user/profile/testprofile"; // 에러 발생 시 리다이렉트
      }
    } else {
      redirectAttributes.addFlashAttribute("errorMessage", "당신 누구야."); // 인증 실패 메시지
      return "redirect:" + request.getHeader("Referer"); // 기존 뷰로 리디렉션
    }
  }
}