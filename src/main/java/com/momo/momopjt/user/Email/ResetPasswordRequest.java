package com.momo.momopjt.user.Email;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResetPasswordRequest {

  private String resetPasswordUserId;
  private String resetPasswordEmail;

}