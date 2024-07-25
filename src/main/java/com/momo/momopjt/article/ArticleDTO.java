package com.momo.momopjt.article;

import com.momo.momopjt.club.Club;
import com.momo.momopjt.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ArticleDTO {

  private Long articleNo;

  private String articleTitle;

  private String articleContent;

  private Instant articleCreateDate = Instant.now(); // 기본 값 설정

  private Character articleState=0;

  private Integer articleScore;

  private Club clubNo;

  private User userNo;

  // 첨부파일 이름들 640
  private List<String> fileNames;
  //Article 에서 Set<ArticleImage> 로 변경 되어야 함

}