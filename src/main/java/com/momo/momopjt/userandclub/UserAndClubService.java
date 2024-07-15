package com.momo.momopjt.userandclub;

import com.momo.momopjt.club.Club;
import java.util.List;

public interface UserAndClubService {
  void joinClub(UserAndClubDTO userAndClubDTO);

  void approveJoin(Long id);

  void leaveClub(Long id);

  List<UserAndClubDTO> readAllMembers(Club clubNo);

  List<UserAndClubDTO> readAllJoinList(Club clubNo);

  void deleteAllMembers(Long clubNo);

  Boolean isLeader(UserAndClubDTO userAndClubDTO);

  int countMembers(Club clubNo);
}