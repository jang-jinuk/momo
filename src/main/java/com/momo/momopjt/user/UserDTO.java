package com.momo.momopjt.user;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Email;
import java.time.Instant;
import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserDTO {


    private Long userNo;

    private String userId;

    private String userPw;

    @Email
    private String userEmail;

    private String userNickname;

    private Character userGender;

    private Integer userAge;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate userBirth;

    private String  userCategory;

    private String userAddress;

    private String userMBTI;

    private Character userState = '0';

    private Character userSocial='M';

    private String userPhoto = ""; // 기본값 설정

    private Integer userLikeNumber= 0; // 기본값 설정

    private Instant userCreateDate;

    private Instant userModifyDate;

    private String confirmUserPw;
}