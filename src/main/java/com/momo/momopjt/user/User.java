package com.momo.momopjt.user;

import lombok.*;

import javax.persistence.*;
import java.time.Instant;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
//@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = "roleSet")

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


    @ElementCollection(fetch = FetchType.LAZY)
    @Builder.Default
    private Set<UserRole> roleSet = new HashSet<>();
    //private Set<UserRole> roleSet = new HashSet<>();는
    // Java 코드에서 roleSet이라는 이름의 Set 컬렉션을 선언하고 초기화하는 것을 의미
    //중복불가
    public void changePassword(String userPw){
        this.userPw = userPw;
    }
    public void changeEmail(String userEmail){
        this.userEmail = userEmail;
    }

    public void changeNickname(String userNickname){
        this.userNickname = userNickname;
    }

    public void addRole(UserRole userRole){
        this.roleSet.add(userRole);
    }

    public void clearRoles(){
        this.roleSet.clear();
    }

    public void changeSocial(char userSocial){
        this.userSocial = userSocial;
    }






}