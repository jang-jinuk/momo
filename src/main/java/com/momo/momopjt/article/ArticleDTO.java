package com.momo.momopjt.article;

import com.momo.momopjt.club.Club;
import com.momo.momopjt.club.QClub;
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
  private Instant articleCreateDate = Instant.now(); // 기본 값 설정
  private Character articleState;
  private Integer articleScore;
  private Club clubNo;
  private User userNo;
}