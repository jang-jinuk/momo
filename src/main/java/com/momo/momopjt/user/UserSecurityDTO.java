package com.momo.momopjt.user;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

@Getter
@Setter
@ToString
public class UserSecurityDTO implements UserDetails {

    private String userId;
    private String userPw;
    private String userEmail;
    private boolean enabled; // 유저 상태
    private Character userSocial; // 'K' for Kakao, 'N' for Naver, 'G' for Google, etc.
    private Collection<? extends GrantedAuthority> authorities;

    public UserSecurityDTO(String username, String password, String email,
                           boolean enabled, Character userSocial,
                           Collection<? extends GrantedAuthority> authorities) {
        this.userId = username;
        this.userPw = password;
        this.userEmail = email;
        this.enabled = enabled;
        this.userSocial = userSocial;
        this.authorities = authorities;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return userPw;
    }

    @Override
    public String getUsername() {
        return userId;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }
}