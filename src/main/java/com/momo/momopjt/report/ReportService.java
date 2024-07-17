package com.momo.momopjt.report;

import com.momo.momopjt.user.User;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ReportService {
  //신고 생성
  void addReport(ReportDTO reportDTO);
  //유저 자기가 신고한 reporterNO로 조회
  List<ReportDTO> readReport(User reporterNo);
  //전부 조회
  List<ReportDTO> readAllReport();
  //유저 제제(수정)
  void userBanReport(ReportDTO reportDTO);
  //유저 제제해제(수정)
  void safeReport(ReportDTO reportDTO);
  //상태 수정후 삭제
  void deleteReport(Long reportNo);
}
