package com.momo.momopjt.alarm;

import com.momo.momopjt.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.Instant;
import java.util.stream.Collectors;
import java.util.List;
import java.util.Optional;


@Service
@Transactional
public class AlarmServiceImpl implements AlarmService {

  private final AlarmRepository alarmRepository;
  private final NotificationService notificationService; // (쏘켓예정)

  @Autowired
  public AlarmServiceImpl(AlarmRepository alarmRepository, NotificationService notificationService) {
    this.alarmRepository = alarmRepository;
    this.notificationService = notificationService;
  }

  @Override
  public void createAlarm(Long userNo, String alarmType, String alarmContent) {
    Alarm alarm = new Alarm();
    User user = new User();
    user.setUserNo(userNo);
    alarm.setUserNo(user);
    alarm.setIsRead('0');
    alarm.setAlarmType(alarmType);
    alarm.setAlarmContent(alarmContent);
    alarm.setAlarmCreateDate(Instant.now());

    Alarm savedAlarm = alarmRepository.save(alarm);

    // 알람 생성 후 알림 전송
    sendNotification(convertToDTO(savedAlarm));
  }


  //Alarm 엔티티의 식별자(alarmNo)를 사용하여 해당 알람을 데이터베이스에서 조회
  // 그 결과를 AlarmDTO로 변환하여 Optional로 감싸서 반환
  @Override
  public Optional<AlarmDTO> getAlarmById(Long alarmNo) {
    return alarmRepository.findById(alarmNo).map(this::convertToDTO);
  }

  @Override            //모든 알람 보기
  public List<AlarmDTO> getAllAlarms() {
    return alarmRepository.findAll().stream()
        .map(this::convertToDTO)
        .collect(Collectors.toList());
  }

  @Override        //알람 업데이트
  public void updateAlarm(Long alarmNo, AlarmDTO alarmDTO) {
    Optional<Alarm> optionalAlarm = alarmRepository.findById(alarmNo);
    if (optionalAlarm.isPresent()) {
      Alarm alarm = optionalAlarm.get();
      alarm.setIsRead(alarmDTO.getIsRead());
      alarm.setAlarmType(alarmDTO.getAlarmType());
      alarm.setAlarmContent(alarmDTO.getAlarmContent());
      alarm.setAlarmCreateDate(alarmDTO.getAlarmCreateDate());
      alarmRepository.save(alarm);
    }
  }

  @Override       //알람 지우기
  public void deleteAlarm(Long alarmNo) {
    alarmRepository.deleteById(alarmNo);
  }
  //Alarm 엔티티 객체를 AlarmDTO 객체로 변환
  private AlarmDTO convertToDTO(Alarm alarm) {
    AlarmDTO dto = new AlarmDTO();
    dto.setAlarmNo(alarm.getAlarmNo());
    dto.setUserNo(alarm.getUserNo().getUserNo());
    dto.setIsRead(alarm.getIsRead());
    dto.setAlarmType(alarm.getAlarmType());
    dto.setAlarmContent(alarm.getAlarmContent());
    dto.setAlarmCreateDate(alarm.getAlarmCreateDate());
    return dto;
  }

  private void sendNotification(AlarmDTO alarmDTO) {
    // 알림 전송 로직 구현
    notificationService.send(alarmDTO); // 필요에 따라 NotificationService와 협력
  }
}