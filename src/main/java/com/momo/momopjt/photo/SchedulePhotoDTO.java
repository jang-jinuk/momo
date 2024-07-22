/*
 *
 * Test 중임. 꼭 필요한 파일이 아님 0718 YY
 *
 */

package com.momo.momopjt.photo;

import com.momo.momopjt.club.Club;
import com.momo.momopjt.schedule.ScheduleDTO;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.Instant;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
public class SchedulePhotoDTO extends ScheduleDTO {

  private final Long scheduleNo;
  private final Club clubNo;
  private final String scheduleTitle;
  private final String scheduleContent;
  private final String schedulePhoto;
  private final Integer scheduleMax;
  private final Integer scheduleParticipants;
  private final String schedulePlace;
  private final Instant scheduleStartDate;


  public SchedulePhotoDTO(Long scheduleNo, Club clubNo, String scheduleTitle, String scheduleContent, String schedulePhoto, Integer scheduleMax, Integer scheduleParticipants, String schedulePlace, Instant scheduleStartDate) {
    super(scheduleNo, clubNo, scheduleTitle, scheduleContent, schedulePhoto, scheduleMax, scheduleParticipants, schedulePlace, scheduleStartDate);
    this.scheduleNo = scheduleNo;
    this.clubNo = clubNo;
    this.scheduleTitle = scheduleTitle;
    this.scheduleContent = scheduleContent;
    this.schedulePhoto = schedulePhoto;
    this.scheduleMax = scheduleMax;
    this.scheduleParticipants = scheduleParticipants;
    this.schedulePlace = schedulePlace;
    this.scheduleStartDate = scheduleStartDate;
  }

  private MultipartFile schedulePhotoFile;

}
