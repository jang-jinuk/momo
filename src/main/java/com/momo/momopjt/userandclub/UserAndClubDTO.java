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
  //todo 0725 즐겨찾기 No or Yes => N / Y  notNull 처리는 어떤지? 기본값 N SW
  private Character isWish = 'N';

  private User userNo;

  private Club clubNo;

  private String userPhotoStr;
}