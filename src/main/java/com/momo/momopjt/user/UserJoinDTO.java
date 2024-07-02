package com.momo.momopjt.user;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;


@Data
public class UserJoinDTO {
    //유효성 검사 추가
    @NotBlank(message = "User ID is required")
    private String userId;

    @NotBlank(message = "Password is required")
    private String userPw;

    @NotBlank(message = "Nickname is required")
    private String userNickname;

    @NotNull(message = "Gender is required")
    private Character userGender;

    @NotNull(message = "Birth date is required")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate userBirth;

    @Email(message = "Email should be valid")
    @NotBlank(message = "Email is required")
    private String userEmail;

    @NotBlank(message = "Category is required")
    private String  userCategory;

    @NotBlank(message = "Address is required")
    private String userAddress;

    @NotBlank(message = "MBTI is required")
    private String userMbti;
}