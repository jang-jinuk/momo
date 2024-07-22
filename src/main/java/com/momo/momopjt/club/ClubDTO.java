package com.momo.momopjt.club;

import com.momo.momopjt.photo.Photo;
import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
