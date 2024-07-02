package com.momo.momopjt.club;

import com.momo.momopjt.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

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
