package com.momo.momopjt.report;

import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;

@Service
@Transactional
@Log4j2
public class ReportServiceImpl implements ReportService {
  @Autowired
  private ReportRepository reportRepository;
  @Autowired
  private ModelMapper modelMapper;

//생성
  @Override
  public void addReport(Report report) {
    //Report 를 받아와서 db에 저장한다.
    reportRepository.save(report);
  }
 //조회
  @Override
  public Report readReport(Long id) {
    return reportRepository.findById(id).orElseThrow();
  }
// 수정
  @Override
  public void updateReport(ReportDTO reportDTO) {
    if(reportRepository.findById(reportDTO.getId()).isPresent()){
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
