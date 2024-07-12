package com.momo.momopjt.alarm;



import java.util.List;
import java.util.Optional;

public interface AlarmService {
  void createAlarm(Long userNo, String alarmType, String alarmContent);

  Optional<AlarmDTO> getAlarmById(Long id);

  List<AlarmDTO> getAllAlarms();

  void updateAlarm(Long id, AlarmDTO alarmDTO);

  void deleteAlarm(Long id);
}
