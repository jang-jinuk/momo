package com.momo.momopjt.report;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
@Log4j2
public class ReportServiceImpl implements ReportService {
  @Autowired
  private ReportRepository reportRepository;

  @Override
  public void addReport(Report report) {

    reportRepository.save(report);
  }

  @Override
  public Report readReport(Long id) {

    return reportRepository.findById(id).orElseThrow();
  }

  @Override
  public List<Report> listReport() {

    return reportRepository.findAll();
  }

  @Override
  public void updateReport(Report report) {

    reportRepository.save(report);
  }

  @Override
  public void deleteReport(Long id) {

  reportRepository.deleteById(id);
  }
}
