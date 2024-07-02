package com.momo.momopjt.user;

import lombok.Data;
import lombok.extern.log4j.Log4j2;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;

@Log4j2
@Data
public class UserJoinDTO {
    //유효성 검사 추가
    @NotBlank(message = "User ID is required")
    @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*\\d)[a-zA-Z0-9]{6,12}$", message = "6에서 12까지 영문 숫자.")
    private String userId;

    @NotBlank(message = "Password is required")
    @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[!@#$%^&*(),.?\":{}|<>])([a-zA-Z0-9!@#$%^&*(),.?\":{}|<>]){8,16}$",
        message = "영문 숫자 특수문자 8 ~ 16")
    private String userPw;

    @NotBlank(message = "Nickname is required")
    @Pattern(regexp = "^[가-힣a-zA-Z0-9]{3,6}$", message = "지켜라")
    private String userNickname;

    @NotNull(message = "Gender is required")
    private Character userGender;

    @NotNull(message = "Birth date is required")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate userBirth;

    @Email(message = "Email should be valid")//이 어노테이션은 믿을게 못돼요
    @NotBlank(message = "Email is required")
    @Pattern(regexp = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$", message = "지키라고")//그래서 추가한 정규식입니다
    private String userEmail;

    @NotBlank(message = "Category is required")
    private String userCategory;

    @NotBlank(message = "Address is required")
    private String userAddress;

    @NotBlank(message = "MBTI is required")
    @Pattern(regexp = "^[IE][NS][TF][PJ]$", message = "MBTI가 뭔지 모르시나요?")//각 자리마다 2단어중 하나 고르게
    private String userMbti;
}
