package com.momo.momopjt.userAndClub;

import com.momo.momopjt.club.Club;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserAndClubRepository extends JpaRepository<UserAndClub, Long> {
 //특정 모임의 맴버(모임원) 정보를 불러오는 쿼리문
 @Query("SELECT u FROM UserAndClub u WHERE u.clubNo = :clubNo AND u.isLeader = :isLeader")
 List<UserAndClub> findMemberList(@Param("clubNo") Club clubNo, @Param("isLeader") Boolean isLeader);

}
