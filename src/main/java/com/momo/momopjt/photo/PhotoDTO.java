package com.momo.momopjt.photo;

import com.momo.momopjt.article.Article;
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
  private String photoUUID;

  private Long photoSize;

  private Instant photoCreateDate;

  private String photoOriginalName;

  private String photoSaveName;

  private String photoThumbnail;

  private byte[] photoData;

  private Article articleNo;

  private User userNo;

}