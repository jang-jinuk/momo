package com.momo.momopjt.user;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.Instant;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "user", schema = "momodb")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    private enum userAdmin{
        USER, ADMIN
    };

}