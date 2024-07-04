package com.momo.momopjt.user;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.Instant;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "user")
public class User {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "user_no", nullable = false)
  private Long userNo;

  @Column(name = "user_id", nullable = false, length = 30)
  private String userId;

  @Column(name = "user_pw", nullable = false)
  private String userPw;

  @Column(name = "user_email", nullable = false, length = 100)
  private String userEmail;

  @Column(name = "user_nickname", nullable = false, length = 100)
  private String userNickname;

  @Column(name = "user_gender", nullable = false)
  private Character userGender;

  @Column(name = "user_birth", nullable = false)
  private LocalDate userBirth;

  @Column(name = "user_category")
  private String userCategory;

  @Column(name = "user_address")
  private String userAddress;

  @Column(name = "user_mbti", length = 4)
  private String userMbti;

  @Column(name = "user_state", nullable = false)
  private Character userState;

  @Column(name = "is_social", nullable = false)
  private Character isSocial;

  @Column(name = "user_photo", nullable = false)
  private String userPhoto;

  @Column(name = "user_like_number")
  private Integer userLikeNumber;

  @Column(name = "user_create_date", nullable = false)
  private Instant userCreateDate;

  @Column(name = "user_modify_date")
  private Instant userModifyDate;

}