package com.momo.momopjt.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private Long userNo;
    private String userId;
    private String userPw;
    private String userEmail;
    private String userNickname;
    private Character userGender;
    private Integer userAge;
    private LocalDate userBirth;
    private String  userCategory;
    private String userAddress;
    private String userMbti;
    private Character userState;
    private Character userSocial;
    private String userPhoto;
    private Integer userLikeNumber;
    private Instant userCreateDate;
    private Instant userModifyDate;
}