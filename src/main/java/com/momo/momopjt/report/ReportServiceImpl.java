package com.momo.momopjt.report;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
@Log4j2
public class ReportServiceImpl implements ReportService {
  @Override
  public void addReport(Report report) {

  }

  @Override
  public Report readReport(Long id) {
    return null;
  }

  @Override
  public List<Report> listReport() {
    return List.of();
  }

  @Override
  public void updateReport(Long id) {

  }

  @Override
  public void deleteReport(Long id) {

  }
}
