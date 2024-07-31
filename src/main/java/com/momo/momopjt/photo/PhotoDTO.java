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

  private String photoExtension;

  private Instant photoCreateDate;

  private User uploader;

  private Character tag; // 현재사용 x
}


