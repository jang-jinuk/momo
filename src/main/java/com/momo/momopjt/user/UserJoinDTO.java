package com.momo.momopjt.user;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
public class UserJoinDTO {

    private String userId;
    private String userPw;
    private String userNickname;
    private Character userGender;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate userBirth;
    private String userEmail;
    private String  userCategory;
    private String userAddress;
    private String userMbti;
}
