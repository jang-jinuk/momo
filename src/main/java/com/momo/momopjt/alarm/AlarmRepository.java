package com.momo.momopjt.alarm;

import com.momo.momopjt.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
@Repository
public interface AlarmRepository extends JpaRepository<Alarm, Long> {
  @Query("SELECT a FROM Alarm a WHERE a.userNo = :userNo ORDER BY a.alarmCreateDate DESC")
  List<Alarm> findByUserNoOrderByAlarmCreateDateDesc(@Param("userNo") User userNo);
  void deleteByUserNo(User user);
}