package com.momo.momopjt.user;

import lombok.Getter;
import lombok.Setter;

public class FindUserIdRequest {

  private String findUserEmail;

  // Getter and Setter
  public String getFindUserEmail() {
    return findUserEmail;
  }

  public void setFindUserEmail(String findUserEmail) {
    this.findUserEmail = findUserEmail;
  }
}
