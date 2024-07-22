package com.momo.momopjt.news;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NewsDTO {

  private Long newsNo;

  private String newsTag;

  private String newsTitle;

  private String newsContent;

  private Instant newsCreateDate;

  private Instant newsModifyDate;

}
