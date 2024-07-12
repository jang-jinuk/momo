package com.momo.momopjt.alarm;



import com.momo.momopjt.user.User;

import java.util.List;
import java.util.Optional;

public interface AlarmService {
  void createAlarm(Long userNo, String alarmType, String alarmContent);
  Optional<AlarmDTO> getAlarmById(Long id);
  List<AlarmDTO> getAllAlarms();
  void updateAlarm(AlarmDTO alarmDTO);
  void deleteAlarm(Long id);

  // 필요한 기능 ?
  // 회원이 자기한테 온 알람만 조회 : 전체알람에서 USER로 필터

  // 회원이 자기한테 온 알람을 읽음 : UPDATE에서 IsRead만 업데이트.
  void isReadUpdate(User user, Long alarmNo);

}
