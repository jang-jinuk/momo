package com.momo.momopjt.userandclub;

import com.momo.momopjt.club.Club;
import java.util.List;

public interface UserAndClubService {
  void joinClub(UserAndClubDTO userAndClubDTO);

  void approveJoin(UserAndClubDTO userAndClubDTO);

  void leaveClub(UserAndClubDTO userAndClubDTO);

  List<UserAndClubDTO> readAllMembers(Club clubNo);

  List<UserAndClubDTO> readAllJoinList(Club clubNo);

  void deleteAllMembers(Long clubNo);

  int isMember(UserAndClubDTO userAndClubDTO);

  int countMembers(Club clubNo);

  UserAndClubDTO isLeader(Club clubNo);
}