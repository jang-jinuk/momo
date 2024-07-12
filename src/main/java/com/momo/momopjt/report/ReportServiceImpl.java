package com.momo.momopjt.report;

import com.momo.momopjt.user.User;
import com.momo.momopjt.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
@Log4j2
public class ReportServiceImpl implements ReportService{
  private final ReportRepository reportRepository;
  private final ModelMapper modelMapper;
  private final UserRepository userRepository;

  //생성
  @Override
  public void addReport(Report report) {
    //Report 를 받아와서 db에 저장한다.
    reportRepository.save(report);
  }
  //자신 유저 아이디로 조회
  /*@Override
  public List<ReportDTO> readReport(ReportDTO reporterNo) {
    Optional<Report> userport = reportRepository.findById(Report);
    Report userports = userport.orElseThrow();
    List<Report> listports = userports;
    return listports.stream()
        .map(report -> modelMapper.map(listports, ReportDTO.class))
        .collect(Collectors.toList());
  }*/
  @Override
  public List<ReportDTO> readReport(User reporterNo) {
    List<Report> reports = reportRepository.myReport(reporterNo);
    return reports.stream()
        .map(report -> modelMapper.map(report, ReportDTO.class))
        .collect(Collectors.toList());
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
//    userRepository.findById(userid).update()
  }
// 삭제
@Override
public void deleteReport(Long reportNo) {
  reportRepository.deleteById(reportNo);
}
}
