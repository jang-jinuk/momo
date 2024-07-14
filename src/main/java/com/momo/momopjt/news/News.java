package com.momo.momopjt.news;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "news")
public class News {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "news_no", nullable = false)
  private Long newsNo;

  @Column(name = "news_tag", nullable = false, length = 10)
  private String newsTag;

  @Column(name = "news_title", nullable = false, length = 50)
  private String newsTitle;

  @Column(name = "news_content", nullable = false, length = 500)
  private String newsContent;

  @Column(name = "news_create_date", nullable = false)
  private Instant newsCreateDate;

  @Column(name = "news_modify_date")
  private Instant newsModifyDate;

}