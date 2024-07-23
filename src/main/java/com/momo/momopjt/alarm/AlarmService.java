package com.momo.momopjt.alarm;

import com.momo.momopjt.club.Club;
import com.momo.momopjt.schedule.Schedule;
import com.momo.momopjt.club.Club;
import com.momo.momopjt.schedule.Schedule;
import com.momo.momopjt.user.User;

import java.util.List;
import java.util.Optional;

public interface AlarmService {

  List<Alarm> getAlarmsByUserId(User user);
  //모임 알람
  void createClubCreatedAlarm(User user, Club club);
  void createClubDeletedAlarm(User user, Club club);

  void createJoinApprovalAlarm(User user, Club club);
  void createLeaveAlarm(User user, Club club);


  //일정알람
  void createParticipateAlarm(User user, Schedule schedule);
  void createCancelParticipateAlarm(User user, Schedule schedule);

  void deleteAlarm(Long alarmNo);

}
