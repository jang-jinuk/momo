package com.momo.momopjt.report;

import com.momo.momopjt.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface ReportRepository extends JpaRepository<Report, Long> {
  @Query("select r from Report r where r.reporterNo =:reporterNo")
  List<Report> myReport(@Param("reporterNo")User reporterNo);
}
