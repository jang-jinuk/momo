package com.momo.momopjt.club;

import java.util.List;

public interface UserAndClubService {
    void joinClub(UserAndClubDTO userAndClubDTO);
    void approveJoin(Long id);
    void disband(Long id);
    List<UserAndClubDTO> readMembers(Long clubNo);
}
