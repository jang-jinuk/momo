package com.momo.momopjt.schedule;

import com.momo.momopjt.club.Club;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.Instant;
import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "schedule")
public class Schedule {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "schedule_no", nullable = false)
  private Long scheduleNo;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "club_no", nullable = false)
  private Club clubNo;

  @Column(name = "schedule_photo", nullable = false)
  private String schedulePhoto;

  @Column(name = "schedule_max", nullable = false)
  private Integer scheduleMax;

  @Column(name = "schedule_place")
  private String schedulePlace;

  @Column(name = "schedule_start_date", nullable = false)
  private Instant scheduleStartDate;

  @OneToMany(mappedBy = "scheduleNo")
  private Set<UserAndSchedule> userAndSchedules = new LinkedHashSet<>();

}