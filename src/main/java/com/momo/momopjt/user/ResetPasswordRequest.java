package com.momo.momopjt.user;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResetPasswordRequest {

  private String resetPasswordUserId;
  private String resetPasswordEmail;
  private String resetPassword;
  private String resetPasswordConfirm;

}