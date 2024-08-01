package com.momo.momopjt.alarm;

import com.momo.momopjt.user.User;
import com.momo.momopjt.user.UserRepository;
import com.momo.momopjt.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;



@Controller
@RequestMapping("/alarm")
public class AlarmController {

  private final AlarmService alarmService;
  private final UserRepository userRepository;
  private UserService userService;

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

  @PostMapping("/delete/{alarmNo}")
  public ResponseEntity<Void> deleteAlarm(@PathVariable Long alarmNo) {
    alarmService.deleteAlarm(alarmNo);
    return ResponseEntity.ok().build();
  }

  // 모든 알림 삭제
  @PostMapping("/deleteAll")
  public ResponseEntity<Void> deleteAllAlarmsForCurrentUser() {
    alarmService.deleteAllAlarmsForCurrentUser();
    return ResponseEntity.ok().build();
  }

}

