package com.momo.momopjt.alarm;

import com.momo.momopjt.article.Article;
import com.momo.momopjt.club.Club;
import com.momo.momopjt.schedule.Schedule;
import com.momo.momopjt.user.User;
import com.momo.momopjt.alarm.Alarm;
import com.momo.momopjt.user.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Service
public class AlarmServiceImpl implements AlarmService {

  private final AlarmRepository alarmRepository;
  private final UserServiceImpl userServiceImpl;

  @Autowired
  public AlarmServiceImpl(AlarmRepository alarmRepository, UserServiceImpl userServiceImpl) {
    this.alarmRepository = alarmRepository;
    this.userServiceImpl = userServiceImpl;
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

  //일정주최자가 일정 생성시 뜨는 알람
  @Override
  public void createScheduleCreatedAlarm(User user, Schedule schedule) {
    Alarm alarm = new Alarm();
    alarm.setUserNo(user);
    alarm.setAlarmType(AlarmType.CREATE);
    alarm.setAlarmContent(schedule.getScheduleTitle() + " 일정을 생성하셨습니다.");
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
    alarm.setAlarmContent(schedule.getScheduleTitle() + "일정에 참가하셨습니다.");
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

  //일정주최자가 일정 삭제시 뜨는 알람
  @Override
  public void createScheduleDeletedAlarm(User user, Schedule schedule) {
    Alarm alarm = new Alarm();
    alarm.setUserNo(user);
    alarm.setAlarmType(AlarmType.DELETE);
    alarm.setAlarmContent(schedule.getScheduleTitle() + " 일정을 삭제하셨습니다.");
    alarm.setAlarmCreateDate(Instant.now());
    alarm.setIsRead('0');
    alarmRepository.save(alarm);
  }


  //회원이 후기글을 생성할때 뜨는 알람
  @Override
  public void createArticleCreatedAlarm(User user, Article article) {
    Alarm alarm = new Alarm();
    alarm.setUserNo(user);
    alarm.setAlarmType(AlarmType.CREATE);
    String userName = user.getUserNickname();
    alarm.setAlarmContent(article.getArticleTitle() + "'이(가) 작성되었습니다.");
    alarm.setAlarmCreateDate(Instant.now());
    alarm.setIsRead('0');
    alarmRepository.save(alarm);
  }

  //후기글 작성자가 후기글을 삭제할때 쓰는 알람
  public void createArticleDeletedAlarm(User user, Article article) {
    Alarm alarm = new Alarm();
    alarm.setUserNo(user);
    alarm.setAlarmType(AlarmType.DELETE);
    alarm.setAlarmContent("후기글 '" + article.getArticleTitle() + "'이(가) 삭제되었습니다.");
    alarm.setAlarmCreateDate(Instant.now());
    alarm.setIsRead('0');
    alarmRepository.save(alarm);
  }

  //누군가 후기글에 댓글을 작성했을때 쓰는 알람
  @Override
  public void createCommentAddedAlarm(User user, Article article) {
    Alarm alarm = new Alarm();
    alarm.setUserNo(user);
    alarm.setAlarmType(AlarmType.COMMENT); // COMMENT 유형의 알림
    alarm.setAlarmContent("귀하의 후기글 \"" + article.getArticleTitle() + "\"에 댓글이 달렸습니다.");
    alarm.setAlarmCreateDate(Instant.now());
    alarm.setIsRead('0');
    alarmRepository.save(alarm);
  }


  //알람 삭제 기능
  @Override
  public void deleteAlarm(Long alarmNo) {
    alarmRepository.deleteById(alarmNo);
  }

  @Override
  @Transactional
  public void deleteAllAlarmsForCurrentUser() {
    User currentUser = userServiceImpl.getCurrentUser();
    if (currentUser != null) {
      alarmRepository.deleteByUserNo(currentUser);
    }
  }

}