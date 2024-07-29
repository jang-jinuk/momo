package com.momo.momopjt.photo;

import com.momo.momopjt.user.User;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.Instant;


@Getter
@Setter
@Entity
@Table(name = "photo")
public class Photo {
  @Id
  @Size(max = 36)
  @Column(name = "photo_uuid", nullable = false)
  private String photoUUID;

  @NotNull
  @Column(name = "photo_create_date", nullable = false)
  private Instant photoCreateDate;

  @Size(max = 255)
  @NotNull
  @Column(name = "photo_extension")
  private String photoExtension;

  @NotNull
  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "user_no", nullable = false)
  private User uploader;

  @Column(name = "photo_tag")
  private Character tag; // User, Club, Schedule, Article 속성 어디에 필요한 것인지 명시


  //toString custom : uuid+extension 해서 파일 이름을 String 타입으로 return
  @Override
  public String toString() {
    return photoUUID+photoExtension;
  }
}