package com.momo.momopjt.user;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

@Getter
@Setter
@ToString
public class UserSecurityDTO extends User {

    private String userId;
    private String userPw;
    private String userEmail;
    private boolean userState; // 유저상태
    private boolean userSocial;

    public UserSecurityDTO(String username, String password, String email,
                           boolean userState, boolean userSocial,
                           Collection<? extends GrantedAuthority> authorities){

        super(username, password, authorities);

        this.userId = username;
        this.userPw = password;
        this.userEmail = email;
        this.userState = userState;
        this.userSocial = userSocial;
    }
}
