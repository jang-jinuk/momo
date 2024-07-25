package com.momo.momopjt.photo;

import com.momo.momopjt.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PhotoDTO {

  private String photoUUID;

  private String photoURL;

  private Instant photoCreateDate;

  private String photoExtension;

  private User uploader;

  private Character tag; // User, Club, Schedule, Article 속성 어디에 필요한 것인지 명시

}


