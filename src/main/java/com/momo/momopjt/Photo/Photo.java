package com.momo.momopjt.Photo;

import com.momo.momopjt.user.User;
import java.time.Instant;
import javax.persistence.*;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Entity
@Table(name = "photo")
public class Photo {
  @Id
  @Column(name = "photo_uuid", nullable = false)
  private String photoUuid;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "user_no", nullable = false)
  private User userNo;

  @Column(name = "photo_size", nullable = false)
  private Integer photoSize;

  @Column(name = "photo_create_date", nullable = false)
  private Instant photoCreateDate;

  @Column(name = "photo_original_name", nullable = false)
  private String photoOriginalName;

  @Column(name = "photo_save_name", nullable = false)
  private String photoSaveName;

  @Column(name = "photo_thumbnail")
  private String photoThumbnail;

}