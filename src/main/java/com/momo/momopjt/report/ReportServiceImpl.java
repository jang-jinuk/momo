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
  //유저 제제 (수정)
  @Override
  public void updateReport(ReportDTO reportDTO) {
    Optional<Report> justice = reportRepository.findById(reportDTO.getReportNo());
    if (justice.isPresent()) {
      Report report = justice.get();
      // reportResult 가 1일 경우 2로 변경하여 저장
      if (report.getReportResult() == '1') {
        report.setReportResult('2');
        reportRepository.save(report);
//        userRepository.findById()
      }
    } else {
      if (reportDTO.getReportResult() != '1') {
        log.info("..........[이미 싸늘해진 시체다]..........KSW");
      }
    }
  }
// 삭제
@Override
public void deleteReport(Long reportNo) {
  reportRepository.deleteById(reportNo);
}
}
