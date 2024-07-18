package com.momo.momopjt.report;

import com.momo.momopjt.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface ReportRepository extends JpaRepository<Report, Long> {
  //자기 ID로 신고내역 조회
  @Query("select r from Report r where r.reporterNo =:reporterNo")
  List<Report> myReport(@Param("reporterNo")User reporterNo);
  //페이징 검색
  Page<Report> findAll(Pageable pageable);
}
