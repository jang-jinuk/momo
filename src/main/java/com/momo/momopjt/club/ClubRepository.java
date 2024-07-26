package com.momo.momopjt.club;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface ClubRepository extends JpaRepository<Club, Long> {
  Boolean existsByClubName(String clubName);

  List<Club> findAllById(Iterable<Long> ids);
}


