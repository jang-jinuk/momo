package com.momo.momopjt.schedule;

import com.momo.momopjt.club.Club;
import com.momo.momopjt.userandschedule.UserAndSchedule;
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

  @Column(name = "schedule_title", nullable = false, length = 50)
  private String scheduleTitle;

  @Column(name = "schedule_content", nullable = false, length = 500)
  private String scheduleContent;

  @Column(name = "schedule_photo")
  private String schedulePhotoUUID;

  @Column(name = "schedule_max", nullable = false)
  private Integer scheduleMax;

  @Column(name = "schedule_participants", nullable = false)
  private Integer scheduleParticipants;

  @Column(name = "schedule_place")
  private String schedulePlace;

  @Column(name = "schedule_start_date", nullable = false)
  private Instant scheduleStartDate;

  @OneToMany(mappedBy = "scheduleNo")
  private Set<UserAndSchedule> userAndSchedules = new LinkedHashSet<>();

}