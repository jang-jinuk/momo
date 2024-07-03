package com.momo.momopjt.userAndSchedule;

import com.momo.momopjt.schedule.Schedule;
import com.momo.momopjt.user.User;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "user_and_schedule")
public class UserAndSchedule {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "schedule_and_user_no", nullable = false)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "user_no", nullable = false)
  private User userNo;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "schedule_no", nullable = false)
  private Schedule scheduleNo;

  @Column(name = "participants", nullable = false)
  private Integer participants;

}