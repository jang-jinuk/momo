package com.momo.momopjt.report;

import java.util.List;

public interface ReportService {

  void addReport(Report report);
  ReportDTO readReport(Long reportNo);
  List<ReportDTO> readAllReport();
  void updateReport(ReportDTO reportDTO);
  void deleteReport(Long reportNo);
}
