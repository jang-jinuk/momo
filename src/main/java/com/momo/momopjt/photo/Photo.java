package com.momo.momopjt;

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
  @Size(max = 255)
  @Column(name = "photo_uuid", nullable = false)
  private String photoUuid;

  @NotNull
  @Column(name = "photo_create_date", nullable = false)
  private Instant photoCreateDate;

  @Size(max = 255)
  @NotNull
  @Column(name = "photo_original_name", nullable = false)
  private String photoOriginalName;

  @NotNull
  @Column(name = "photo_size", nullable = false)
  private Integer photoSize;

  @Size(max = 255)
  @Column(name = "photo_thumbnail")
  private String photoThumbnail;

  @NotNull
  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "user_no", nullable = false)
  private User userNo;

}