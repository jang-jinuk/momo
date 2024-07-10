package com.momo.momopjt.report;

import java.util.List;

public interface ReportService {

  void addReport(Report report);
  Report readReport(Long id);
  List<Report> listReport();
  void updateReport(Long id);
  void deleteReport(Long id);
}
