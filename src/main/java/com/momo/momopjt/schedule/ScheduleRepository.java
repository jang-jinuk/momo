package com.momo.momopjt.schedule;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
  @Query("SELECT s.scheduleNo, s.scheduleTitle, s.scheduleContent, s.schedulePhoto, " +
      "s.schedulePlace, s.scheduleStartDate, s.scheduleMax " +
      "FROM Schedule s WHERE s.clubNo = :clubNo And s.scheduleMax != s.scheduleParticipants AND s.scheduleStartDate > current_timestamp")
  List<Schedule> findOngoingSchedules(@Param("clubNo") Long clubNo);
}
