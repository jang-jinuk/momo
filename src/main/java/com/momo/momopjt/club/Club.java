package com.momo.momopjt.club;

import lombok.*;

import javax.persistence.*;
import java.time.Instant;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "club")

public class Club {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "club_no", nullable = false)
  private Long clubNo;

//  @ManyToOne(fetch = FetchType.LAZY)
//  @JoinColumn(name = "club_photo")
//  private Photo clubPhoto;

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

  @Column(name = "club_gender")
  private Character clubGender;

  @Column(name = "club_create_date", nullable = false)
  private Instant clubCreateDate;

  @Column(name = "club_photo")
  private String clubPhotoUUID;



//  @OneToMany(mappedBy = "club", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//  private List<Article> articles;
  //모임 정보 수정 메소드
  public void change(String clubPhotoUUID, String clubCategory, String clubContent,
                     String clubArea, Integer clubMax) {
    this.clubPhotoUUID = clubPhotoUUID;
    this.clubCategory = clubCategory;
    this.clubContent = clubContent;
    this.clubArea = clubArea;
    this.clubMax = clubMax;
  }
}