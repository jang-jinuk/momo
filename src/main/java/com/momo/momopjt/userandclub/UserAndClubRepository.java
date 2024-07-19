package com.momo.momopjt.userandclub;

import com.momo.momopjt.club.Club;

import java.util.List;

import com.momo.momopjt.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserAndClubRepository extends JpaRepository<UserAndClub, Long> {

  //특정 모임의 맴버 정보를 조회
  UserAndClub findByUserNoAndClubNo(User userNo, Club clubNo);

  //특정 모임의 모든 맴버(모임원) 정보를 불러오는 쿼리문
  List<UserAndClub> findMemberByClubNoAndIsLeader(Club clubNo, Boolean isLeader);

  //특정 모임의 가입 신청 내역을 불러오는 쿼리문
  //joinDate가 null이면 가입 신청 상태, 값이 null이지 않으면 가입 승인 상태
  List<UserAndClub> findByClubNoAndJoinDateIsNull(Club clubNo);

  //특정 모임 맴버 전체 삭제(모임 해산 기능)
  @Modifying
  void deleteByClubNo(Club clubNo);

  //특정 모임의 총인원 수
  int countByClubNoAndJoinDateIsNotNull(Club clubNo);

  //특정 맴버 삭제
  @Modifying
  void deleteByClubNoAndUserNo(Club clubNo, User userNo);

  //내가 가입된 모입 조회
  @Query("SELECT u.clubNo FROM UserAndClub u WHERE u.userNo = :userNo AND u.isLeader IS NOT NULL")
  List<Club> findMyClubs(@Param("userNo")User userNo);

}