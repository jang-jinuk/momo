package com.momo.momopjt.article;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = "article")
public class ArticleImage implements Comparable<ArticleImage>{

  //612page
  @Id
  private String uuid;

  private String extension;

  private int ord;

  @ManyToOne
  private Article article;

  @Override
  public int compareTo(ArticleImage other) {
    return this.ord - other.getOrd();
  }

  public void changeArticle(Article article) {
    this.article = article;
  }

}
