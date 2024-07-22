package com.momo.momopjt.report;

import com.momo.momopjt.user.User;
import com.momo.momopjt.user.UserRepository;
import lombok.Data;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Instant;

@Data
@SpringBootTest
@Log4j2
public class ReportServiceTests {

  @Autowired
  private ReportService reportService;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private ModelMapper modelMapper;
  @Autowired
  private ReportRepository reportRepository;

  //    신고했을때 DB에 추가
  @Test
  void reportCreateTest() {
    User user1 = userRepository.findById(3L).orElseThrow();
    User user2 = userRepository.findById(4L).orElseThrow();
// User 객체 생성 및 데이터베이스에 저장

    for(int i=0; i<50; i++) {
      log.info("----------------- [report create test]-----------------");
      Report report = new Report();
      report.setReportNo(1L);
      report.setReportCategory("bug"+i);
      report.setReportDate(Instant.now());
      report.setReportResult('1');
      report.setReportedNo(user1);
      report.setReporterNo(user2);
      reportRepository.save(report);
    }
    log.info("...... [complate]..........KSW");

  }// reportCreateTest()

//    Report report = modelMapper.map(reportDTO, Report.class);
//    reportService.addReport(report); // ReportService 를 사용하여 Report 추가
//  }
    //신고내역 1개 조회
//  @Test
//  void reportReadTest(){
//    log.info("----------------- [report read test]-----------------");
//    Report report = reportService.readReport(1L); // 찾을 신고번호 입력
//    log.info("읽어온 데이따 : "+report.getReportDate().toString());
//    log.info(report.toString());
//
//    log.info("...... [07-10-17:23:10]..........KSW");
//  }
    //신고 수정(상태 수정)
//  @Test
//  void reportUpdateTest() {
//    log.info("----------------- [report update test]-----------------");
//   ReportDTO reportDTO = ReportDTO.builder()
//       .reportNo(3L)
//       .reporterNo(userRepository.findById(2L).orElseThrow())
//       .reportedNo(userRepository.findById(1L).orElseThrow())
//       .reportResult('2')
//       .reportDate(Instant.now())
//       .reportCategory("man")
//       .build();
//    reportService.updateReport(reportDTO);
//  }
////  신고 삭제
//    @Test
//  void reportDeleteTest(){
//    log.info("---------------- [report delete test]---------------");
//    //지울 신고 번호
//    Long numberWillDelete = 2L;
//
//    if (reportService.readReport(numberWillDelete) == null){ // 삭제하기전 존재하는지 확인
//      log.info("test Failed /----------------해당 번호 공지사항 존재 x ");
//    } else {
//      reportService.deleteReport(numberWillDelete);
//      log.info("test Passed");
//      }
//    }
//
//    @Test
//    public void readOneReportTest() {
//      ReportDTO reportDTO = reportService.readReport(1L);
//      log.info(reportDTO);
//
//    }
//      @Test
//      public void allreadTest(){
//        List<ReportDTO> repotr = reportService.readAllReport();
//        log.info(repotr);
//  }

}
