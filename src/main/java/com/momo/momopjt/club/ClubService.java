package com.momo.momopjt.club;

import java.util.List;

public interface ClubService {
    Long createClub(ClubDTO clubDTO);
    ClubDTO readOneClub(Long clubNo);
    List<ClubDTO> readAllClub();
}