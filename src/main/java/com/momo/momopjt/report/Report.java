package com.momo.momopjt.report;

import com.momo.momopjt.user.User;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "report")
public class Report {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "report_no", nullable = false)
  private Long reportNo;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "reporter_no", nullable = false)
  private User reporterNo;   //신고한사람

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "reported_no", nullable = false)
  private User reportedNo;  // 신고당한사람

  @Column(name = "report_category", nullable = false, length = 100)
  private String reportCategory;

  @Column(name = "report_date", nullable = false)
  private Instant reportDate;

  @Column(name = "report_result", nullable = false)
  private Character reportResult;

}