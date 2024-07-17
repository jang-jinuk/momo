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
public class ReportServiceImpl implements ReportService {
  private final ReportRepository reportRepository;
  private final ModelMapper modelMapper;
  private final UserRepository userRepository;

  //신고하기 (추가)
  public void addReport(ReportDTO reportDTO) {
    Report report = modelMapper.map(reportDTO, Report.class);
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
  public void userBanReport(ReportDTO reportDTO) {
    log.info("...... [updateReport START]..........KSW");

    Optional<Report> ban = reportRepository.findById(reportDTO.getReportNo());
    if (ban.isPresent()) {
      log.info("...... [ban present]..........KSW");
      Report report = ban.get();
      // reportResult 가 1일 경우 2로 변경하여 저장

      if (report.getReportResult() == '1') {
        log.info("...... [reportResult == 1]..........KSW");
        report.setReportResult('2');
        // 사용자의 userState 값 변경

        Optional<User> userOptional = userRepository.findById(report.getReportedNo().getUserNo());
        if (userOptional.isPresent()) {
          log.info("...... [userOptional is Present !!]..........KSW");
          User user = userOptional.get();
          user.setUserState('2');
          userRepository.save(user);
        } else {
          if (reportDTO.getReportResult() == '2') {
            log.info("..........[이미 싸늘해진 시체다]..........KSW");
          }
        }
      }
    }
  }
  //유저 제제 해제(수정) 제제랑 숫자만 다름
  @Override
  public void safeReport(ReportDTO reportDTO) {
    log.info("...... [update Report START]..........KSW");
    Optional<Report> sage = reportRepository.findById(reportDTO.getReportNo());
    if (sage.isPresent()) {
      log.info("...... [sage present]..........KSW");
      Report report = sage.get();
      // reportResult 가 2일 경우 1로 변경하여 저장
      if (report.getReportResult() == '2') {
        log.info("...... [report Result == 2]..........KSW");
        report.setReportResult('1');
        // 사용자의 userState 값 변경
        Optional<User> userOptional = userRepository.findById(report.getReportedNo().getUserNo());
        if (userOptional.isPresent()) {
          log.info("...... [userOptional is Present? !!]..........KSW");
          User user = userOptional.get();
          user.setUserState('1');
          userRepository.save(user);
        } else {
          if (reportDTO.getReportResult() == '1') {
            log.info("..........[살아남남]..........KSW");
          }
        }
      }
    }
  }
  // 삭제
  @Override
  public void deleteReport(Long reportNo) {
    Optional<Report> optionalReport = reportRepository.findById(reportNo);
    if (optionalReport.isPresent()) {
      Report report = optionalReport.get();
      if (report.getReportResult() == '1') {
        reportRepository.deleteById(reportNo);
      } else {
        System.out.println("회원 상태가 이상하여 삭제처리가 어렵습니다");
      }
    } else {
      System.out.println("회원을 찾을 수 없습니다!");
    }
  }
  //페이징 검색
  @Override
  public List<ReportDTO> searchReports(String query) {
    List<ReportDTO> allReports = readAllReport(); // 모든 리포트를 조회
    if (query == null || query.isEmpty()) {
      return allReports;
    }
    return allReports.stream() //각 칼럼이 쿼리를 포함하면 리스트로 수집
        .filter(report -> report.getReporterNo().getUserId().contains(query)
            || report.getReportedNo().getUserId().contains(query)
            || report.getReportedNo().getUserNickname().contains(query)
            || report.getReportCategory().contains(query))
        .collect(Collectors.toList());
  }
}


