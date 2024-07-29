package com.momo.momopjt.userandclub;

import com.momo.momopjt.club.Club;
import com.momo.momopjt.user.User;

import java.util.List;

public interface UserAndClubService {

  void joinClub(UserAndClubDTO userAndClubDTO);

  Boolean approveJoin(UserAndClubDTO userAndClubDTO);

  void leaveClub(UserAndClubDTO userAndClubDTO);

  List<UserAndClubDTO> readAllMembers(Club clubNo);

  List<UserAndClubDTO> readAllJoinList(Club clubNo);

  void deleteAllMembers(Long clubNo);

  int isMember(UserAndClubDTO userAndClubDTO);

  int countMembers(Club clubNo);

  UserAndClubDTO findLeader(Club clubNo);

  //모임 즐겨찾기 조회
  List<Club> findMyWishClubs(User user);

  //모임 즐겨찾기 상태 업데이트
  void updateWishClub(UserAndClubDTO userAndClubDTO);
}