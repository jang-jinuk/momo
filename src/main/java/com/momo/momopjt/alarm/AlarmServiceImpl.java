package com.momo.momopjt.alarm;

import com.momo.momopjt.club.Club;
import com.momo.momopjt.club.ClubRepository;
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
  public void createJoinApprovalAlarm(User user, Club club) {
    Alarm alarm = new Alarm();
    alarm.setUserNo(user);
    alarm.setAlarmType(AlarmType.JOIN);
    alarm.setAlarmContent(club.getClubName()+"에 가입되셨습니다.");
    alarm.setAlarmCreateDate(Instant.now());
    alarm.setIsRead('0');
    alarmRepository.save(alarm);
  }
  @Override
  public void deleteAlarm(Long alarmNo) {
    alarmRepository.deleteById(alarmNo);
  }
}