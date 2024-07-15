package com.momo.momopjt.article;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ArticleDTO {
  private Long articleNo;
  private String articleTitle;
  private String articleContent;
  private Instant articleCreateDate = Instant.now(); // 기본 값 설정
  private Character articleState;
  private Integer articleScore;
  private Long clubNo;
  private Long userNo;
}