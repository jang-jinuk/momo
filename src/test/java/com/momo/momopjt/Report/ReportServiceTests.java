package com.momo.momopjt.Report;

import com.momo.momopjt.report.Report;
import com.momo.momopjt.report.ReportDTO;
import com.momo.momopjt.report.ReportService;
import com.momo.momopjt.user.User;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Instant;
import java.util.List;

@SpringBootTest
@Log4j2
public class ReportServiceTests {
  @Autowired
  private ReportService reportService;
  Instant now = Instant.now();

  //신고했을때 DB에 추가
  @Test
  void reportCreateTest() {
    // User 객체 생성 및 데이터베이스에 저장
    User user1 = new User();
    user1.setUserNo(1L); // 데이터베이스에 저장 후 반환값으로 갱신
    User user2 = new User();
    user2.setUserNo(2L); // 데이터베이스에 저장 후 반환값으로 갱신
    Report report = new Report();
    log.info("----------------- [news create test]-----------------");
    ReportDTO reportDTO = ReportDTO.builder()
        .id(1L)
        .reporterNo(user1)
        .reportedNo(user2)
        .reportCategory("spam")
        .reportDate(now)
        .reportResult('0')
        .build();
    reportService.addReport(report); // ReportService 를 사용하여 Report 추가
  }
  //신고내역 1개 조회
  @Test
  void reportReadTest(){
    log.info("----------------- [report read test]-----------------");
    Report reportReadTest = reportService.readReport(1L); // 찾을 공지 번호 입력
    log.info("read ed report : "+reportReadTest);
    log.info("...... [07-10-17:23:10]..........KSW");
  }
  //신고내역 모두 조회
  @Test
  void reportListAllTest(){
    log.info("----------------- [report list test]-----------------");
    List<Report> reportList = reportService.listReport();
    log.info("reportList : "+reportList);
    log.info("...... [07-10-17:23:47]..........KSW");
  }
  //신고 수정(상태 수정)
//  @Test
//  void reportUpdateTest() {
//    log.info("----------------- [report update test]-----------------");
//    //1번 정보를 2번에다가 업데이트
//    Report willUpdateReport = reportService.readReport(1L);
//
//    log.info("willUpdateNews modifyDate (1L) : " + willUpdateReport.getReportResult());
//
//    //Result 추가
//    willUpdateReport.setReportResult();
//    willUpdateReport.setId(2L);
//    reportService.updateReport(willUpdateReport);
//
//    Report reportCheckUpdated = reportService.readReport (2L);
//    log.info("reportCheckUpdated ReportResult (2L) : " + reportCheckUpdated.getReportResult());
//
//  }
  //신고 삭제
    @Test
  void reportDeleteTest(){
    log.info("---------------- [report delete test]---------------");
    //지울 공지 넘버 입력
    Long numberWillDelete = 3L;

    if (reportService.readReport(numberWillDelete) == null){ // 삭제하기전 존재하는지 확인
      log.info("test Failed /----------------해당 번호 공지사항 존재 x ");
    } else {
      reportService.deleteReport(numberWillDelete);
      log.info("test Passed");
    }
  }
  }
