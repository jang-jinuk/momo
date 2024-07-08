package com.momo.momopjt.user;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResetPasswordRequest {
  private String userId;
  private String email;
  private String password;
  private String confirmPassword;
}