package com.momo.momopjt.club;

public interface UserAndClubService {
    void joinClub(UserAndClubDTO userAndClubDTO);
    void approveJoin(Long id);
}
