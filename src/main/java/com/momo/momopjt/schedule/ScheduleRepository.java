package com.momo.momopjt.schedule;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
//  @Query("SELECT s.scheduleNo, s.clubNo, s.scheduleTitle, s.scheduleContent, s.schedulePhoto, " +
//      "s.schedulePlace, s.scheduleStartDate, s.scheduleMax, s. " +
//      "FROM Schedule s " +
//      "JOIN UserAndSchedule u WHERE s.clubNo = :clubNo " +
//      "AND s.scheduleMax > s.scheduleParticipants " +
//      "AND s.scheduleStartDate > :instant")
//  List<Schedule> findSchedules(@Param("clubNo") Long clubNo, @Param("instant") Instant instant);



}
