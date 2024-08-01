package com.momo.momopjt.schedule;

import com.momo.momopjt.club.Club;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
  //시간이 마감되지 않은 일정 조회
  @Query("SELECT s FROM Schedule s WHERE s.clubNo = :clubNo " +
      "AND s.scheduleStartDate > current_timestamp")
  List<Schedule> findOngoingSchedules(@Param("clubNo") Club clubNo);

  //시간이 마감된 일정 조회
  @Query("SELECT s FROM Schedule s WHERE s.clubNo = :clubNo " +
      "AND s.scheduleStartDate < current_timestamp")
  List<Schedule> findEndSchedules(@Param("clubNo") Club clubNo);

  //해당 모임에 모든 일정 조회
  @Query("SELECT s FROM Schedule s WHERE s.clubNo = :clubNo")
  List<Schedule> findSchedulesByClub(@Param("clubNo") Club clubNo);
}
