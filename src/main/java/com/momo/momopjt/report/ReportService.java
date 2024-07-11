package com.momo.momopjt.report;

public interface ReportService {

  void addReport(Report report);
  Report readReport(Long id);
  void updateReport(ReportDTO reportDTO);
  void deleteReport(Long id);
}
