package com.momo.momopjt.report;

import com.momo.momopjt.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReportDTO {

  private Long id;

  private User reporterNo;

  private User reportedNo;

  private String reportCategory;

  private Instant reportDate;

  private Character reportResult;
}
