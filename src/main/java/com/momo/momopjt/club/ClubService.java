package com.momo.momopjt.club;

import com.momo.momopjt.Photo.PhotoDTO;

import java.util.List;

public interface ClubService {
  Long createClub(ClubDTO clubDTO, PhotoDTO photoDTO);

  ClubDTO readOneClub(Long clubNo);

  List<ClubDTO> readAllClub();

  Long updateClub(ClubDTO clubDTO, PhotoDTO photoDTO);

  void disbandClub(Long clubNo);
}