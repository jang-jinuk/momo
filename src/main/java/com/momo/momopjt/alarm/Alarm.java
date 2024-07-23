package com.momo.momopjt.alarm;

import com.momo.momopjt.user.User;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "alarm", schema = "momodb")
public class Alarm {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "alarm_no", nullable = false)
  private Long alarmNo;

  @ManyToOne
  @JoinColumn(name = "user_no")
  private User userNo;

  @Column(name = "is_read", nullable = false)
  private Character isRead;

  @Enumerated(EnumType.STRING)
  @Column(name = "alarm_type", length = 100)
  private AlarmType alarmType;

  @Column(name = "alarm_content")
  private String alarmContent;

  @Column(name = "alarm_create_date")
  private Instant alarmCreateDate;
}