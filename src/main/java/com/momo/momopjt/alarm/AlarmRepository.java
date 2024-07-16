package com.momo.momopjt.alarm;

import com.momo.momopjt.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface AlarmRepository extends JpaRepository<Alarm, Long> {

  @Query("select a from Alarm a where a.userNo =:userNo")
  List<Alarm> findAlarmByUserNo(@Param("userNo") User userNo);

}