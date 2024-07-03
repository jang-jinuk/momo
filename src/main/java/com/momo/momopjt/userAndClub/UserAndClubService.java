package com.momo.momopjt.userAndClub;

import com.momo.momopjt.club.Club;

import java.util.List;

public interface UserAndClubService {
    void joinClub(UserAndClubDTO userAndClubDTO);
    void approveJoin(Long id);
    void disband(Long id);
    List<UserAndClubDTO> readAllMembers(Club clubNo, Boolean isLeader);
}
