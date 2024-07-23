package com.momo.momopjt.alarm;

import com.momo.momopjt.club.Club;
import com.momo.momopjt.user.User;

import java.util.List;
import java.util.Optional;

public interface AlarmService {

  List<Alarm> getAlarmsByUserId(User user);
  void createJoinApprovalAlarm(User user, Club club);
  void deleteAlarm(Long alarmNo);
}
