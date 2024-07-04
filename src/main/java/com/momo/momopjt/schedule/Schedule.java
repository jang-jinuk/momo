package com.momo.momopjt.schedule;

import com.momo.momopjt.club.Club;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "schedule")
public class Schedule {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "schedule_no", nullable = false)
  private Long id;

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

  @Column(name = "schedule_title", nullable = false, length = 50)
  private String scheduleTitle;

  @Column(name = "schedule_content", length = 500)
  private String scheduleContent;

}