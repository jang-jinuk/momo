package com.momo.momopjt.club;

import com.momo.momopjt.photo.Photo;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "club")
public class Club {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "club_no", nullable = false)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "club_photo", nullable = false)
  private Photo clubPhoto;

  @Column(name = "club_name", nullable = false, length = 50)
  private String clubName;

  @Column(name = "club_category", length = 100)
  private String clubCategory;

  @Column(name = "club_content")
  private String clubContent;

  @Column(name = "club_area", length = 50)
  private String clubArea;

  @Column(name = "club_max", nullable = false)
  private Integer clubMax;

  @Column(name = "club_gender", nullable = false)
  private Character clubGender;

  @Column(name = "club_create_date", nullable = false)
  private Instant clubCreateDate;

}