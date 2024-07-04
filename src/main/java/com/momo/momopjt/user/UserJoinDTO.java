package com.momo.momopjt.user;

import lombok.Data;
import lombok.extern.log4j.Log4j2;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Log4j2
@Data
public class UserJoinDTO {
    private String userId;
    private String userPw;
    private String userEmail;
    private String userNickname;
    @NotNull(message = "Gender is required")
    private Character userGender;
    @NotNull(message = "Birth date is required")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate userBirth;
    private String userCategory;
    private String userAddress;
    private String userMbti;
    private Character userSocial = 'M';
}