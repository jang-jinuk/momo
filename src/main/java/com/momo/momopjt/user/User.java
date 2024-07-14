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
    private String userMBTI;
    private Character userState = '0'; // 기본값 설정
    private Character userSocial = 'M'; // 'K' for Kakao, 'N' for Naver, 'G' for Google, etc
    private String userPhoto = ""; // 기본값 설정
    private Integer userLikeNumber = 0; // 기본값 설정
    private Instant userCreateDate;
    private Instant userModifyDate;


    @ElementCollection(fetch = FetchType.LAZY)
    @Builder.Default
    private Set<UserRole> roleSet = new HashSet<>();

    // 비밀번호 변경 메서드
    public void changePassword(String userPw){this.userPw = userPw;
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
    public void changeUserSocial(Character userSocial){
        if(userSocial != 'K' && userSocial != 'N' && userSocial != 'G' && userSocial != 'M'){
            throw new IllegalArgumentException("Invalid social type");
        }
        this.userSocial = userSocial;
    }
}