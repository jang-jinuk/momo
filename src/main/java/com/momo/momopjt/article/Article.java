package com.momo.momopjt.article;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "article")
public class Article {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "article_no", nullable = false)
  private Long id;

  @Column(name = "article_title", nullable = false, length = 50)
  private String articleTitle;

  @Column(name = "article_content", nullable = false, length = 500)
  private String articleContent;

  @Column(name = "article_create_date", nullable = false)
  private Instant articleCreateDate;

  @Column(name = "article_state", nullable = false)
  private Character articleState;

  @Column(name = "article_score")
  private Integer articleScore;

}