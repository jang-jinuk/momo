package com.momo.momopjt.alarm;


import com.momo.momopjt.club.ClubDTO;
import com.momo.momopjt.user.User;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Log4j2
public class alarmServiceTest {

  @Autowired
  private AlarmRepository alarmRepository;

  @Autowired
  private NotificationService notificationService;

  @Autowired
  private AlarmServiceImpl alarmService;

  private Alarm alarm;
  private AlarmDTO alarmDTO;


  @BeforeEach
  public void setUp() {


    alarmService = new AlarmServiceImpl(alarmRepository, notificationService);

    // 테스트 실행 전에 모든 알람 삭제
   // alarmRepository.deleteAll();
    User user = new User();
    user.setUserNo(1L);

    alarm = new Alarm();
    alarm.setAlarmNo(1L);
    alarm.setUserNo(user);
    alarm.setIsRead('0');
    alarm.setAlarmType("모임가입완료");
    alarm.setAlarmContent("유정이네 모임에 가입되셨습니다.");
    alarm.setAlarmCreateDate(Instant.now());

    alarmDTO = new AlarmDTO();
    alarmDTO.setAlarmNo(1L);
    alarmDTO.setUserNo(1L);
    alarmDTO.setIsRead('0');
    alarmDTO.setAlarmType("모임가입완료");
    alarmDTO.setAlarmContent("유정이네 모임에 가입되셨습니다.");
    alarmDTO.setAlarmCreateDate(Instant.now());
  }

  @Test
  public void 새로운알람생성() {
    alarmService.createAlarm(3L, "강퇴", "먹방모임에서 강퇴당하셨습니다.");

    List<Alarm> alarms = alarmRepository.findAll();
    //assertEquals(1, alarms.size()); (알람이 n개인지 확인하는 거 )
    log.info("-------- [07-12-13:11:35]-------you");
    Alarm createdAlarm = alarms.get(0);

    //assertEquals("승인", createdAlarm.getAlarmType());
    //assertEquals("어서오세요", createdAlarm.getAlarmContent());
  }


  @Test
  public void 특정알람조회() {
    Long alarmNo = 13L;

    Optional<AlarmDTO> alarmDTOOptional = alarmService.getAlarmById(alarmNo);
    //log.info(alarmDTOOptional);
    assertTrue(alarmDTOOptional.isPresent(), "AlarmDTO should be present");
  }

  @Test
  public void 모든알람조회(){
    alarmRepository.save(alarm);

    List<AlarmDTO> result = alarmService.getAllAlarms();
    log.info(result);

  }

  @Test
  public void 알람업데이트() {
    // Given
    Long alarmNo = 13L;
    AlarmDTO updatedAlarmDTO = new AlarmDTO();
    updatedAlarmDTO.setIsRead('1');
    updatedAlarmDTO.setAlarmType("TypeB");
    updatedAlarmDTO.setAlarmContent("Updated Content");

    // When
    alarmService.updateAlarm(alarmNo, updatedAlarmDTO);

    // Then
    Optional<Alarm> updatedAlarmOptional = alarmRepository.findById(alarmNo);
    assertTrue(updatedAlarmOptional.isPresent(), "Updated Alarm should be present");


    }

  @Test
  public void 알람삭제기능() {

    Long alarmNo = 13L;
    alarmService.deleteAlarm(alarmNo);
  }
}




