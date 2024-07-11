package com.momo.momopjt.report;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
@Log4j2
public class ReportServiceImpl implements ReportService{
  private final ReportRepository reportRepository;
  private final ModelMapper modelMapper;

  //생성
  @Override
  public void addReport(Report report) {
    //Report 를 받아와서 db에 저장한다.
    reportRepository.save(report);
  }
  //조회
  @Override
  public ReportDTO readReport(Long reportNo) {
    Report report = reportRepository.findById(reportNo).orElseThrow();
    ReportDTO reportDTO = modelMapper.map(report, ReportDTO.class);
    return reportDTO;
  }
  //모두 조회
  @Override
  public List<ReportDTO> readAllReport() {
    // 모든 리포트를 조회하여 리스트에 저장
    List<Report> reports = reportRepository.findAll();

    // Report 리스트를 ReportDTO 리스트로 변환
    return reports.stream()
        .map(report -> modelMapper.map(report, ReportDTO.class))
        .collect(Collectors.toList());
  }

  //수정
  @Override
  public void updateReport(ReportDTO reportDTO) {
    if(reportRepository.findById(reportDTO.getReportNo()).isPresent()){
      Report report = modelMapper.map(reportDTO, Report.class);
      reportRepository.save(report);
    } else {
      log.info("...... [업데이트 존재 x ]..........KSW");
    }
  }
// 삭제
@Override
public void deleteReport(Long id) {
  reportRepository.deleteById(id);
}
}
