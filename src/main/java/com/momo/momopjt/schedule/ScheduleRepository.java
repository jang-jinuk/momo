package com.momo.momopjt.schedule;

import com.momo.momopjt.club.Club;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
  @Query("SELECT s FROM Schedule s WHERE s.clubNo = :clubNo " +
      "AND s.scheduleStartDate > current_timestamp")
  List<Schedule> findOngoingSchedules(@Param("clubNo") Club clubNo);
}
