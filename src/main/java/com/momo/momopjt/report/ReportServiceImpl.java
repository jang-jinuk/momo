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

  private final UserRepository userRepository;

  private final ModelMapper modelMapper;

  //신고하기 (추가)
  public void addReport(ReportDTO reportDTO) {
    Report report = modelMapper.map(reportDTO, Report.class);
    reportRepository.save(report);
  }

  //자신 유저 아이디로 조회
  @Override
  public List<ReportDTO> readReport(User reporterNo) {
    // 사용자가 신고한 내역 조회
    List<Report> reports = reportRepository.myReport(reporterNo);

    return reports.stream()
        .map(report -> {// ReportDTO로 변환
          ReportDTO reportDTO = modelMapper.map(report, ReportDTO.class);
          // User 정보 설정
          reportDTO.setReporterNo(report.getReporterNo()); // 신고자 정보
          return reportDTO;
        })
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
  public void deactivateUser(ReportDTO reportDTO) {
    log.info("...... [updateReport START]..........KSW");

    Optional<Report> suspend = reportRepository.findById(reportDTO.getReportNo());
    if (suspend.isPresent()) {
      log.info("...... [ban present]..........KSW");
      Report report = suspend.get();
      // reportResult 가 1일 경우 2로 변경하여 저장

        Optional<User> userOptional = userRepository.findById(report.getReportedNo().getUserNo());
        if (userOptional.isPresent()) {
          log.info("...... [userOptional is Present !!]..........KSW");
          User user = userOptional.get();
          user.setUserState('1');

          userRepository.save(user);
        }
    }
  }
  //유저 제제 해제(수정) 제제랑 숫자만 다름
  @Override
  public void reactivateUser(ReportDTO reportDTO) {
    log.info("...... [update Report START]..........KSW");

    Optional<Report> reactivate = reportRepository.findById(reportDTO.getReportNo());
    if (reactivate.isPresent()) {
      log.info("...... [sage present]..........KSW");
      Report report = reactivate.get();
      // reportResult 가 2일 경우 1로 변경하여 저장
        Optional<User> userOptional = userRepository.findById(report.getReportedNo().getUserNo());
        if (userOptional.isPresent()) {
          log.info("...... [userOptional is Present? !!]..........KSW");
          User user = userOptional.get();
          user.setUserState('0');

          userRepository.save(user);
        }
    }
  }
  // 삭제
  @Override
  public void deleteReport(Long reportNo) {
        reportRepository.deleteById(reportNo);
    }

  //페이징 검색
  @Override
  public List<ReportDTO> searchReports(String query) {
    List<ReportDTO> allReports = readAllReport(); // 모든 리포트를 조회
    if (query == null || query.isEmpty()) {
      return allReports;
    }
    return allReports.stream() //각 칼럼이 쿼리를 포함하면 리스트로 수집
        .filter(report ->
            report.getReporterNo().getUserId().contains(query) ||
                report.getReportedNo().getUserId().contains(query) ||
                report.getReportedNo().getUserNickname().contains(query) ||
                report.getReportCategory().contains(query))
        .collect(Collectors.toList());
  }
}


