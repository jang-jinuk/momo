package com.momo.momopjt.article;

import com.momo.momopjt.club.Club;
import com.momo.momopjt.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ArticleDTO {

  private Long articleNo;

  private String articleTitle;

  private String articleContent;

  private Instant articleCreateDate;

  private Character articleState=0;

  private Integer articleScore;

  private Club clubNo;

  private User userNo;

  private String articlePhotoUUID;

}