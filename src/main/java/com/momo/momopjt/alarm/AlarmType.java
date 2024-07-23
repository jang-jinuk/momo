package com.momo.momopjt.alarm;

public enum AlarmType {

  JOIN("가입"),
  LEAVE("탈퇴하셨습니다"),
  LIKE("좋아요가 눌렸습니다"),
  REPORT("신고가 접수되었습니다"),
  KICKOUT("강퇴되었습니다");
  private final String message;

  AlarmType(String message) {
    this.message = message;
  }

  public String getMessage() {
    return message;
  }
}
