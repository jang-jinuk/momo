package com.momo.momopjt.club;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClubDTO {

  private Long clubNo;

  private String clubName;

  private String clubCategory;

  private String clubContent;

  private String clubArea;

  private Integer clubMax;

  private Character clubGender;

  private Instant clubCreateDate;

  //YY 모임 대표 이미지
  private String clubPhotoUUID; // uuid , no extension


}
