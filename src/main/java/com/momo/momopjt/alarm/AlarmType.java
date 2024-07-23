package com.momo.momopjt.alarm;

public enum AlarmType {

  JOIN("가입"),
  LEAVE("탈퇴"),
  LIKE("좋아요"),
  REPORT("신고"),
  KICKOUT("강퇴");
  private final String message;

  AlarmType(String message) {
    this.message = message;
  }

  public String getMessage() {
    return message;
  }
}
