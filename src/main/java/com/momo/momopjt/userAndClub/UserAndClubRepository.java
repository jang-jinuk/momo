package com.momo.momopjt.userAndClub;

import com.momo.momopjt.club.Club;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface UserAndClubRepository extends JpaRepository<UserAndClub, Long> {
 //특정 모임의 맴버(모임원) 정보를 불러오는 쿼리문
 @Query("SELECT u FROM UserAndClub u WHERE u.clubNo = :clubNo AND u.isLeader = :isLeader")
 List<UserAndClub> findMemberList(@Param("clubNo") Club clubNo, @Param("isLeader") Boolean isLeader);

 //특정 모임의 가입 신청 내역을 불러오는 쿼리문
 //joinDate가 null이면 가입 신청 상태, 값이 null이지 않으면 가입 승인 상태
 @Query("SELECT u FROM UserAndClub u WHERE u.clubNo =: clubNo AND u.joinDate =: null")
 List<UserAndClub> findJoinList(@Param("clubNo") Club clubNo);
}
