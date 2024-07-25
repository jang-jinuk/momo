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

  //모임 생성시 모임장이 받는 알람
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

  //모임 삭제시 모임장이 받는 알람
  @Override
  public void createClubDeletedAlarm(User user, Club club) {
    Alarm alarm = new Alarm();
    alarm.setUserNo(user);
    alarm.setAlarmType(AlarmType.DELETE);
    alarm.setAlarmContent(club.getClubName() + " 모임이 삭제되었습니다.");
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

  //모임장이 모임원 강퇴시 유저에게 강퇴되었다는 알림
  @Override
  public void createKickOutAlarm(User user, Club club) {

    Alarm alarm = new Alarm();
    alarm.setUserNo(user);
    alarm.setAlarmType(AlarmType.KICKOUT);
    alarm.setAlarmContent(club.getClubName() + "에서 강퇴되었습니다.");
    alarm.setAlarmCreateDate(Instant.now());
    alarm.setIsRead('0');
    alarmRepository.save(alarm);

  }

  //모임장이 유저 강퇴시 모임장에게 뜨는 알림
  @Override
  public void createKickOutOwnerAlarm(User user, Club club){
    String userNickname = user.getUserNickname();
    Alarm alarm = new Alarm();
    alarm.setUserNo(user);
    alarm.setAlarmType(AlarmType.KICKOUT);
    alarm.setAlarmContent(userNickname + "님을 " + club.getClubName() + "에서 강퇴했습니다.");
    alarm.setAlarmCreateDate(Instant.now());
    alarm.setIsRead('0');
    alarmRepository.save(alarm);
  }

  //회원이 모임 탈퇴시 뜨는 알람
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



  //회원이 일정 참가시 뜨는 알람
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

  //회원이 일정 참가 취소시 뜨는 알람
  @Override
  public void createCancelParticipateAlarm(User user, Schedule schedule) {
    Alarm alarm = new Alarm();
    alarm.setUserNo(user);
    alarm.setAlarmType(AlarmType.CANCEL_PARTICIPATE);
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