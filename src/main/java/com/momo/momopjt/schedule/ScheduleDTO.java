package com.momo.momopjt.schedule;

import com.momo.momopjt.club.Club;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.Instant;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ScheduleDTO {
  private Long scheduleNo;

  private Club clubNo;

  private String scheduleTitle;

  private String scheduleContent;

  @Builder.Default
  private String schedulePhoto = "default";

  private Integer scheduleMax;

  @Builder.Default
  private Integer scheduleParticipants = 1;

  private String schedulePlace;

  private Instant scheduleStartDate;

}
