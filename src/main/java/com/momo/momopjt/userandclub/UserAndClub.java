package com.momo.momopjt.userandclub;

import com.momo.momopjt.club.Club;
import com.momo.momopjt.user.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "user_and_club")
public class UserAndClub {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "user_and_club_no", nullable = false)
  private Long id;

  @Column(name = "is_leader")
  private Boolean isLeader;

  @Column(name = "join_date")
  private Instant joinDate;

  @Column(name = "is_wish")
  private Character isWish;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "user_no", nullable = false)
  private User userNo;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "club_no", nullable = false)
  private Club clubNo;

  public User getUserNo() {
    return userNo;
  }

  public void setUserNo(User userNo) {
    this.userNo = userNo;
  }

}