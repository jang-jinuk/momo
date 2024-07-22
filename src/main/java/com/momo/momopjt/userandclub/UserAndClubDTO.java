package com.momo.momopjt.userandclub;

import com.momo.momopjt.club.Club;
import com.momo.momopjt.user.User;
import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserAndClubDTO {

  private Long id;

  private Boolean isLeader;

  private Instant joinDate;

  private Character isWish;

  private User userNo;

  private Club clubNo;

}