package com.momo.momopjt.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Email;
import java.time.Instant;
import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {

    private Long userNo;
    //@NotBlank
    private String userId;
    //@NotBlank
    private String userPw;
    @Email
    //@NotBlank
    private String userEmail;
    //@NotBlank
    private String userNickname;
    //@NotNull(message = "Gender is required")
    private Character userGender;
    private Integer userAge;
//    @NotNull(message = "Birth date is required")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate userBirth;
    private String  userCategory;
    private String userAddress;
    private String userMBTI;
    private Character userState;
    private Character userSocial='M';
    private String userPhoto;
    private Integer userLikeNumber;
    private Instant userCreateDate;
    private Instant userModifyDate;
}