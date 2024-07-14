package com.momo.momopjt.report;

import com.momo.momopjt.user.User;

import java.util.List;

public interface ReportService {
  //생성
  void addReport(Report report);
  //유저 자기가 신고한 내역 ID로 조회
  List<ReportDTO> readReport(User reporterNo);
  //전부 조회
  List<ReportDTO> readAllReport();
  //상태 수정
  void updateReport(ReportDTO reportDTO);
  //상태 수정후 삭제
  void deleteReport(Long reportNo);
}
