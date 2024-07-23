package com.momo.momopjt.alarm;

import com.momo.momopjt.user.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AlarmDTO {
  private Long alarmNo;
  private User userNo;
  private Character isRead='0'; // 기본값설정 읽으면 1
  private String alarmType; //
  private String alarmContent;
  private Instant alarmCreateDate= Instant.now(); // 기본 값 설정
}
