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
  @Modifying
  @Query("DELETE FROM UserAndSchedule WHERE scheduleNo = :scheduleNo AND userNo = :userNo")
  void deleteParticipant(@Param("scheduleNo") Schedule scheduleNo, @Param("userNo") User userNo);

  @Modifying
  @Query("DELETE FROM UserAndSchedule WHERE scheduleNo = :scheduleNo")
  void deleteAllParticipant(@Param("scheduleNo") Schedule scheduleNo);

  @Query("SELECT us FROM UserAndSchedule us WHERE us.scheduleNo = :scheduleNo")
  List<UserAndSchedule> findByAllParticipants(@Param("scheduleNo") Schedule scheduleNo);

  @Query("SELECT us FROM UserAndSchedule us WHERE us.scheduleNo = :scheduleNo AND us.userNo = :userNo")
  UserAndSchedule findByParticipants(@Param("scheduleNo") Schedule scheduleNo, @Param("userNo") User userNo);
}
