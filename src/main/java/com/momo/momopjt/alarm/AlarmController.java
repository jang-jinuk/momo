package com.momo.momopjt.alarm;

import com.momo.momopjt.user.User;
import com.momo.momopjt.user.UserRepository;
import com.momo.momopjt.user.UserSecurityDTO;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import static org.springframework.security.core.context.SecurityContextHolder.*;


@Controller
@RequestMapping("/alarm")
public class AlarmController {

  private final AlarmService alarmService;
  private final UserRepository userRepository;

  @Autowired
  public AlarmController(AlarmService alarmService, UserRepository userRepository) {
    this.alarmService = alarmService;
    this.userRepository = userRepository;
  }

  @GetMapping("/list")
  public String getUserAlarms(Model model) {
    User currentUser = getCurrentUser(); // 사용자 정보를 가져오는 메서드
    if (currentUser == null) {
      return "redirect:/login"; // 인증되지 않은 경우 로그인 페이지로 리디렉션
    }
    List<Alarm> alarms = alarmService.getAlarmsByUserId(currentUser);
    model.addAttribute("alarms", alarms);
    return "alarm/list";
  }

  private User getCurrentUser() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication != null && authentication.isAuthenticated()) {
      Object principal = authentication.getPrincipal();
      if (principal instanceof UserDetails) {
        String username = ((UserDetails) principal).getUsername();
        // 사용자명을 통해 User 엔티티를 조회하여 반환하는 예시
        return userRepository.findByUserId(username);
      }
    }
    return null;
  }

  @DeleteMapping("/delete/{alarmNo}")
  public ResponseEntity<Void> deleteAlarm(@PathVariable Long alarmNo) {
    alarmService.deleteAlarm(alarmNo);
    return ResponseEntity.ok().build();
  }
}