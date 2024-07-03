package com.momo.momopjt.schedule;

import com.momo.momopjt.club.Club;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.LinkedHashSet;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ScheduleDTO {
  private Long scheduleNo;

  private Club clubNo;

  private String schedulePhoto;

  private Integer scheduleMax;

  private String schedulePlace;

  private Instant scheduleStartDate;

  private Set<UserAndSchedule> userAndSchedules = new LinkedHashSet<>();
}
