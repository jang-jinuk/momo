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

  private String photoURL;

  private Instant photoCreateDate;

  private String photoOriginalName;

  private User uploader; // User의 ID만 전달

  private Character tag; // User, Club, Schedule, Article 속성 어디에 필요한 것인지 명시

}


