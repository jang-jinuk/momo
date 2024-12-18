package com.momo.momopjt.report;

import com.momo.momopjt.user.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
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
@RequiredArgsConstructor
@Log4j2
@Transactional
public class AdminController {

  @Autowired
  private ReportService reportService;

  @Autowired
  private UserService userService;

  @Autowired
  private UserRepository userRepository;



  //조회와 페이징 검색
  @GetMapping("/manage-report")
  public String manageReportGet(Model model,
                             @RequestParam(value = "page", defaultValue = "1") int page,
                             @RequestParam(value = "query", defaultValue = "") String query) {
    log.info(".......... [GET /manage-report]..........KSW");
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    String currentUsername = authentication.getName();

    // 사용자 정보 조회
    User user = userRepository.findByUserId(currentUsername);

    if (user.getUserRole().getValue() != 1) { //
      return "redirect:/"; // 잘못된 접근 시 리다이렉트
    }
    // 검색어를 사용하여 리포트 조회
    List<ReportDTO> findReports;

    if (query.isEmpty()) {
      findReports = reportService.readAllReport(); // 검색어가 비었으면 모든 리포트 조회
    } else {
      findReports = reportService.searchReports(query); // 검색어에 따른 리포트 조회
    }

    //페이지 수 설정
    int totalReports = findReports.size(); // 총 데이터 수
    int pageSize = 10; // 한 번에 표시할 페이지 수
    int lastPage = (totalReports + pageSize - 1) / pageSize; // 총 페이지 수

    // 페이지에 맞게 데이터 나누기
    int fromIndex = (page - 1) * pageSize;
    int toIndex = Math.min(fromIndex + pageSize, totalReports);
    List<ReportDTO> reports = findReports.subList(fromIndex, toIndex);

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

    log.trace("...... [AdminController /manage-report/ END]..........KSW");
    return "admin/manage-report";
  }

  //제제
  @PostMapping("/deactivate-user")
  public String deactivateUserPost(@RequestParam("reportNo") Long reportNo,
                                   @RequestParam("currentPage") int currentPage,
                                   @RequestParam("query") String query) throws UnsupportedEncodingException {
    log.info("...... [POST /deactivate-user]..........KSW");
    ReportDTO reportDTO = new ReportDTO(); // 타입에 맞게 객체를 생성하여
    reportDTO.setReportNo(reportNo); //담아주고
    // reportService.updateReport() 메서드 호출
    reportService.deactivateUser(reportDTO); //기능으로 넘겨준다
    log.info("...... [정의구현]..........KSW");
    log.info("...... [{}]..........KSW",query);
    String encodedQuery = URLEncoder.encode(query, StandardCharsets.UTF_8); //쿼리를 인코더에 담아준다
    return "redirect:/admin/manage-report?page=" + currentPage + "&query=" + encodedQuery;
  }

  //제제해제
  @PostMapping("/reactivate-user")
  public String reactivateUserPost(@RequestParam("reportNo") Long reportNo,
                                   @RequestParam("currentPage") int currentPage,
                                   @RequestParam("query") String query) throws UnsupportedEncodingException {
    log.info("...... [GET /reactivate-user]..........KSW");
    ReportDTO reportDTO = new ReportDTO(); // 타입에 맞게 객체를 생성하여
    reportDTO.setReportNo(reportNo); //담아주고
    reportService.reactivateUser(reportDTO); //기능으로 넘겨준다
    log.info("...... [공소시효 만료]..........KSW");
    String encodedQuery = URLEncoder.encode(query, "UTF-8"); //한글깨짐 방지
    return "redirect:/admin/manage-report?page=" + currentPage + "&query=" + encodedQuery;
  }

  //삭제
  @PostMapping("/delete-report")
  public String deleteReportPost(@RequestParam("reportNo") Long reportNo,
                                 @RequestParam("currentPage") int currentPage,
                                 @RequestParam("query") String query) throws UnsupportedEncodingException{
    log.info("...... [POST /delete-report]..........KSW");
    reportService.deleteReport(reportNo);
    String encodedQuery = URLEncoder.encode(query, StandardCharsets.UTF_8);
    return "redirect:/admin/manage-report?page=" + currentPage + "&query=" + encodedQuery;
  }

  @GetMapping("/manage-user")
  public String manageUserGet(Model model,
                              @RequestParam(value = "page", defaultValue = "1") int page,
                              @RequestParam(value = "query", defaultValue = "") String query) {
    log.info("GET /manage-user");

    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    String currentUsername = authentication.getName();

    // 사용자 정보 조회
    User user = userRepository.findByUserId(currentUsername);

    if (user.getUserRole().getValue() != 1) {
      return "redirect:/"; // 잘못된 접근 시 리다이렉트
    }

    List<UserDTO> findUsers;
    if (query.isEmpty()) {
      findUsers = userService.readALLUsers();
    } else {
      findUsers = userService.searchUsers(query);
    }

    int totalUsers = findUsers.size();
    int pageSize = 10;
    int lastPage = (totalUsers + pageSize - 1) / pageSize;

    int fromIndex = (page - 1) * pageSize;
    int toIndex = Math.min(fromIndex + pageSize, totalUsers);
    List<UserDTO> users = findUsers.subList(fromIndex, toIndex);

    int pageGroupSize = 10;
    int currentGroup = (page - 1) / pageGroupSize;
    int startPage = currentGroup * pageGroupSize + 1;
    int endPage = Math.min(startPage + pageGroupSize - 1, lastPage);

    model.addAttribute("UserDTO", users);
    model.addAttribute("page", page);
    model.addAttribute("lastPage", lastPage);
    model.addAttribute("query", query);
    model.addAttribute("startPage", startPage);
    model.addAttribute("endPage", endPage);

    log.trace("AdminController /manage-user END");
    return "admin/manage-User";
  }

  @PostMapping("/change-userRole")
  public String changeUserrolePost(@RequestParam("userNo") Long userNo,
                                   @RequestParam("currentPage") int currentPage,
                                   @RequestParam("query") String query) throws UnsupportedEncodingException {
    log.info("...... [POST /change-userRole]..........KSW");

    UserDTO userDTO = new UserDTO(); // 타입에 맞게 객체를 생성하여
    userDTO.setUserNo(userNo);

    userService.chageRoleUser(userDTO); //기능으로 넘겨준다
    log.info("...... [권한부여]..........KSW");
    log.info("...... [{}]..........KSW",query);
    String encodedQuery = URLEncoder.encode(query, StandardCharsets.UTF_8); //쿼리를 인코더에 담아준다

    return "redirect:/admin/manage-user?page=" + currentPage + "&query=" + encodedQuery;
  }

}
