package com.momo.momopjt.user.find;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FindPasswordRequest {
  private String userId;
  private String userEmail;

}