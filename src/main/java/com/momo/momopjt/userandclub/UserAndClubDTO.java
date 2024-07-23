package com.momo.momopjt.userandclub;

import com.momo.momopjt.club.Club;
import com.momo.momopjt.user.User;
import java.time.Instant;

import lombok.*;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserAndClubDTO {

  private Long id;

  private Boolean isLeader;

  private Instant joinDate;

  private Character isWish;

  private User userNo;

  private Club clubNo;

}