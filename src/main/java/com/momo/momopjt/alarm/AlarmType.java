package com.momo.momopjt.alarm;

public enum AlarmType {

  CREATE("생성"),
  DELETE("삭제"),
  JOIN("가입"),
  LEAVE("탈퇴"),
  LIKE("좋아요"),
  REPORT("신고"),
  KICKOUT("강퇴"),
  PARTICIPATE("참가"),
  CANCEL_PARTICIPATE("취소"),
  COMMENT("댓글");


  private final String message;

  AlarmType(String message) {
    this.message = message;
  }

  public String getMessage() {
    return message;
  }
}
