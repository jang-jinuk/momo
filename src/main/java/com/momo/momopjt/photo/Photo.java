package com.momo.momopjt.photo;

import com.momo.momopjt.article.Article;
import com.momo.momopjt.club.Club;
import com.momo.momopjt.user.User;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.Instant;
import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "photo")
public class Photo {
  @Id
  @Column(name = "photo_uuid", nullable = false)
  private String photoUUID;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "user_no", nullable = false)
  private User userNo;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "article_no")
  private Article articleNo;

  @Column(name = "photo_size", nullable = false)
  private Integer photoSize;

  @Column(name = "photo_create_date", nullable = false)
  private Instant photoCreateDate;

  @Column(name = "photo_original_name", nullable = false)
  private String photoOriginalName;

  @Column(name = "photo_thumbnail")
  private String photoThumbnail;

  @Column(name = "photo_data")
  private byte[] photoData;

  @OneToMany(mappedBy = "clubPhoto")
  private Set<Club> clubs = new LinkedHashSet<>();

}