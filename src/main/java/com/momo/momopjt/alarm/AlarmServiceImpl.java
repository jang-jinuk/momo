package com.momo.momopjt.alarm;

import com.momo.momopjt.club.Club;
import com.momo.momopjt.club.ClubRepository;
import com.momo.momopjt.schedule.Schedule;
import com.momo.momopjt.user.User;
import com.momo.momopjt.user.UserRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.Instant;
import java.util.stream.Collectors;
import java.util.List;
import java.util.Optional;

@Service
public class AlarmServiceImpl implements AlarmService {

  private final AlarmRepository alarmRepository;

  @Autowired
  public AlarmServiceImpl(AlarmRepository alarmRepository) {
    this.alarmRepository = alarmRepository;
  }

  @Override
  public List<Alarm> getAlarmsByUserId(User user) {
    return alarmRepository.findByUserNo(user);
  }

  @Override
  public void createClubCreatedAlarm(User user, Club club) {
    Alarm alarm = new Alarm();
    alarm.setUserNo(user);
    alarm.setAlarmType(AlarmType.CREATE);
    alarm.setAlarmContent(club.getClubName() + " 모임이 생성되었습니다.");
    alarm.setAlarmCreateDate(Instant.now());
    alarm.setIsRead('0');
    alarmRepository.save(alarm);
  }

  // 모임 가입시 뜨는 알람
  @Override
  public void createJoinApprovalAlarm(User user, Club club) {
    Alarm alarm = new Alarm();
    alarm.setUserNo(user);
    alarm.setAlarmType(AlarmType.JOIN);
    alarm.setAlarmContent(club.getClubName()+"에 가입되셨습니다.");
    alarm.setAlarmCreateDate(Instant.now());
    alarm.setIsRead('0');
    alarmRepository.save(alarm);
  }

  //모임 탈퇴시 뜨는 알람
  @Override
  public void createLeaveAlarm(User user, Club club) {
    Alarm alarm = new Alarm();
    alarm.setUserNo(user);
    alarm.setAlarmType(AlarmType.LEAVE);
    alarm.setAlarmContent(club.getClubName() + "에서 탈퇴하셨습니다.");
    alarm.setAlarmCreateDate(Instant.now());
    alarm.setIsRead('0');
    alarmRepository.save(alarm);
  }

  //일정 참가시 뜨는 알람
  @Override
  public void createParticipateAlarm(User user, Schedule schedule) {
    Alarm alarm = new Alarm();
    alarm.setUserNo(user);
    alarm.setAlarmType(AlarmType.PARTICIPATE);
    alarm.setAlarmContent(schedule.getScheduleTitle() + "에 참가하셨습니다.");
    alarm.setAlarmCreateDate(Instant.now());
    alarm.setIsRead('0');
    alarmRepository.save(alarm);
  }

  @Override
  public void createCancelParticipateAlarm(User user, Schedule schedule) {
    Alarm alarm = new Alarm();
    alarm.setUserNo(user);
    alarm.setAlarmType(AlarmType.CANCEL_PARTICIPATE); // 새로운 알람 타입을 사용합니다.
    alarm.setAlarmContent(schedule.getScheduleTitle() + " 일정 참가를 취소하셨습니다.");
    alarm.setAlarmCreateDate(Instant.now());
    alarm.setIsRead('0');
    alarmRepository.save(alarm);
  }

  //알람 삭제 기능
  @Override
  public void deleteAlarm(Long alarmNo) {
    alarmRepository.deleteById(alarmNo);
  }
}