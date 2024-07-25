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
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ArticleListAllDTO {
  //633p
  private long articleNo;
  private String articleTitle;
  private String articleContent;
  private char articleState;
  private int articleScore;

  private User user;
  private Club club;

  private Instant articleCreateDate;

  //TODO check
  private long replyCount;

  private List<ArticleImageDTO> articleImages;

}
