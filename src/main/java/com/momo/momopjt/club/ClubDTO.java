package com.momo.momopjt.club;

import com.momo.momopjt.photo.Photo;
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

  private Photo photoUUID;

  private String clubName;

  private String clubCategory;

  private String clubContent;

  private String clubArea;

  private Integer clubMax;

  private Character clubGender;

  private Instant clubCreateDate;



}
