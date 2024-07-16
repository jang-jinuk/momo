package com.momo.momopjt.club;

import com.momo.momopjt.photo.PhotoDTO;
import com.momo.momopjt.userandclub.UserAndClubDTO;

import java.util.List;

public interface ClubService {
  Long createClub(ClubDTO clubDTO, PhotoDTO photoDTO, UserAndClubDTO userAndDTO);

  ClubDTO readOneClub(Long clubNo);

  List<ClubDTO> readAllClub();

  Boolean updateClub(ClubDTO clubDTO, PhotoDTO photoDTO);

  void disbandClub(Long clubNo);
}