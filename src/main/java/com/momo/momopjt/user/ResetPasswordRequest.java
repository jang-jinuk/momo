package com.momo.momopjt.user;

import lombok.*;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ResetPasswordRequest {
  private String userId;
  private String email;
  private String password;
  private String confirmPassword;
}