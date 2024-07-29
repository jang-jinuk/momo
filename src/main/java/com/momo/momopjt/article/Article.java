package com.momo.momopjt.article;

import com.momo.momopjt.club.Club;
import com.momo.momopjt.user.User;
import lombok.*;
import org.hibernate.annotations.BatchSize;

import javax.persistence.*;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "article", schema = "momodb")
@ToString(exclude = "imageSet")
public class Article {
  //extends BaseEntity 는 추상클래스 435P

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "article_no", nullable = false)
  private Long articleNo;

  @Column(name = "article_title", nullable = false, length = 50)
  private String articleTitle;

  @Column(name = "article_content", nullable = false, length = 500)
  private String articleContent;

  @Column(name = "article_create_date", nullable = false, updatable = false)
  private Instant articleCreateDate = Instant.now(); // 기본값 설정

  @Column(name = "article_state", nullable = false)
  private Character articleState; // 기본값 설정

  @Column(name = "article_score")
  private Integer articleScore; // 기본값 설정

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "club_no")
  private Club clubNo;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_no")
  private User userNo;

  //YY
  @OneToMany(mappedBy = "article",
      cascade = {CascadeType.ALL},
      fetch = FetchType.LAZY,
      orphanRemoval = true
  )
  @Builder.Default
  @BatchSize(size = 20) // N번의 쿼리 한번에 실행
  private Set<ArticleImage> imageSet = new HashSet<>();

  // Article 업데이트 메서드
  public void update(ArticleDTO articleDTO, Club clubNo) {
    this.articleTitle = articleDTO.getArticleTitle();
    this.articleContent = articleDTO.getArticleContent();
    this.articleState = articleDTO.getArticleState();
    this.articleScore = articleDTO.getArticleScore();
    this.clubNo = articleDTO.getClubNo();
    this.userNo = articleDTO.getUserNo();
  }

  public void addImage(String uuid, String extension){
    ArticleImage articleImage = ArticleImage.builder()
        .uuid(uuid)
        .extension(extension)
        .article(this)
        .ord(imageSet.size())
        .build();
    this.imageSet.add(articleImage);
  }

  public void clearImages(){
    imageSet.forEach(articleImage -> articleImage.changeArticle(null));
    this.imageSet.clear();
  }



}

/*
* 원본
*
* package com.momo.momopjt.article;

import com.momo.momopjt.club.Club;
import com.momo.momopjt.user.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.Instant;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "article", schema = "momodb")
public class Article {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "article_no", nullable = false)
  private Long articleNo;

  @Column(name = "article_title", nullable = false, length = 50)
  private String articleTitle;

  @Column(name = "article_content", nullable = false, length = 500)
  private String articleContent;

  @Column(name = "article_create_date", nullable = false, updatable = false)
  private Instant articleCreateDate = Instant.now(); // 기본값 설정

  @Column(name = "article_state", nullable = false)
  private Character articleState; // 기본값 설정

  @Column(name = "article_score")
  private Integer articleScore; // 기본값 설정

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "club_no")
  private Club clubNo;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_no")
  private User userNo;

  // Article 업데이트 메서드
  public void update(ArticleDTO articleDTO, Club clubNo) {
    this.articleTitle = articleDTO.getArticleTitle();
    this.articleContent = articleDTO.getArticleContent();
    this.articleState = articleDTO.getArticleState();
    this.articleScore = articleDTO.getArticleScore();
    this.clubNo = clubNo;
  }
}
* */