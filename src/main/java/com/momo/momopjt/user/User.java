package com.momo.momopjt.user;

import lombok.*;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;
import java.time.Instant;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = "roleSet")
@DynamicInsert
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

    private Character userSocial = 'M'; // 'K' for Kakao, 'N' for Naver, 'G' for Google, etc

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
    // 비밀번호 변경 메서드
    public void changePassword(String userPw){
        this.userPw = userPw;
    }
    // 이메일 변경 메서드
    public void changeEmail(String userEmail){
        this.userEmail = userEmail;
    }
    // 닉네임 변경 메서드
    public void changeNickname(String userNickname){
        this.userNickname = userNickname;
    }
    // 역할 추가 메서드
    public void addRole(UserRole userRole){
        this.roleSet.add(userRole);
    }
    // 역할 초기화 메서드
    public void clearRoles(){
        this.roleSet.clear();
    }
    // 소셜 타입 변경 메서드
    public void changeSocial(char userSocial){
        this.userSocial = userSocial;
    }






}