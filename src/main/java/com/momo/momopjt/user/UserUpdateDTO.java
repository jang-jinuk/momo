package com.momo.momopjt.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserUpdateDTO {
  @NotBlank
  private String userId;
  @NotBlank
  private String userPw;
  @Email
  @NotBlank
  private String userEmail;
  @NotBlank
  private String userNickname;
  @NotBlank
  private String userCategory;
  @NotBlank
  private String userAddress;
  @NotBlank
  private String userMbti;
}