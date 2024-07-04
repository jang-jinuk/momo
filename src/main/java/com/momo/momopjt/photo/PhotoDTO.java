package com.momo.momopjt.photo;

import com.momo.momopjt.user.User;
import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PhotoDTO {
  private String photoUuid;

  private User userNo;

  private Integer photoSize;

  private Instant photoCreateDate;

  private String photoOriginalName;

  private String photoSaveName;

  private String photoThumbnail;
}
