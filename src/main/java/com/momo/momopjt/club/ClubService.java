package com.momo.momopjt.club;

import com.momo.momopjt.user.User;
import com.momo.momopjt.userandclub.UserAndClubDTO;

import java.util.List;

public interface ClubService {

  Long createClub(ClubDTO clubDTO, UserAndClubDTO userAndDTO) throws ClubNameException; // 0729 YY photoDTO 제거

  ClubDTO readOneClub(Long clubNo);

  List<ClubDTO> readAllClub();

  Boolean updateClub(ClubDTO clubDTO);

  void deleteClub(Long clubNo);

  List<ClubDTO> readMyClubs(User userNo);

  class ClubNameException extends Exception {}

}