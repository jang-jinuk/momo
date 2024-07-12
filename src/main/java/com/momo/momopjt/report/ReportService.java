package com.momo.momopjt.report;

import com.momo.momopjt.user.User;

import java.util.List;

public interface ReportService {
  //생성
  void addReport(Report report);
  //유저 ID로 조회
  List<ReportDTO> readReport(User reporterNo);
  //전부 조회
  List<ReportDTO> readAllReport();
  //수정
  void updateReport(ReportDTO reportDTO);
  //삭제
  void deleteReport(Long reportNo);
}
