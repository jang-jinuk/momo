package com.momo.momopjt.club;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserAndClubRepository extends JpaRepository<UserAndClub, Long> {
  @Query("SELECT u FROM UserAndClub u WHERE u.clubNo = :clubNo")
  List<UserAndClub> findMemberList(@Param("clubNo") Long clubNo);
}
