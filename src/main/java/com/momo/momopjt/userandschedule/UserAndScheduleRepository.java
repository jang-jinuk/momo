package com.momo.momopjt.userandschedule;

import com.momo.momopjt.schedule.Schedule;
import com.momo.momopjt.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserAndScheduleRepository extends JpaRepository<UserAndSchedule, Long> {
  //일정에 참가자를 삭제하는 쿼리문
  @Modifying
  @Query("DELETE FROM UserAndSchedule WHERE scheduleNo = :scheduleNo AND userNo = :userNo")
  void deleteParticipant(@Param("scheduleNo") Schedule scheduleNo, @Param("userNo") User userNo);

  //일정에 모든 참가자를 삭제하는 쿼리문
  @Modifying
  @Query("DELETE FROM UserAndSchedule WHERE scheduleNo = :scheduleNo")
  void deleteAllParticipant(@Param("scheduleNo") Schedule scheduleNo);

  //일정에 참석한 모든 참가자를 조회하는 쿼리문
  @Query("SELECT us FROM UserAndSchedule us WHERE us.scheduleNo = :scheduleNo")
  List<UserAndSchedule> findByAllParticipants(@Param("scheduleNo") Schedule scheduleNo);

  //일정에 참석한 인원인지 확인하는 쿼리문
  @Query("SELECT us FROM UserAndSchedule us WHERE us.scheduleNo = :scheduleNo AND us.userNo = :userNo")
  UserAndSchedule findByParticipant(@Param("scheduleNo") Schedule scheduleNo, @Param("userNo") User userNo);

  //특정 회원이 주최한 모든 일정 조회
  @Query("SELECT  us.scheduleNo FROM UserAndSchedule us WHERE us.userNo = :userNo AND us.isHost = true")
  List<Schedule> findSchedulesHostedByUser(@Param("userNo")User userNo);

  //특정 회원이 참석한 모든 일정 조회
  @Query("SELECT  us.scheduleNo FROM UserAndSchedule us WHERE us.userNo = :userNo AND us.isHost = false")
  List<Schedule> findSchedulesParticipatedByUser(@Param("userNo")User userNo);
}
