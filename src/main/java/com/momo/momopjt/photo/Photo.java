package com.momo.momopjt.photo;

import com.momo.momopjt.user.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

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


  @Size(max = 255)
  @NotNull
  @Column(name = "photo_extension")
  private String photoExtension;

  @NotNull
  @Column(name = "photo_create_date", nullable = false)
  private Instant photoCreateDate;

  @NotNull
  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "user_no", nullable = false)
  private User uploader;

  @Column(name = "photo_tag")
  private Character tag; // 현재 사용 x


  //toString custom : uuid+extension 해서 파일 이름을 String 타입으로 return
  @Override
  public String toString() {
    return photoUUID+photoExtension;
  }
}