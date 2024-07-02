package com.momo.momopjt.user;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Map;

@Getter
@Setter
@ToString
public class UserSecurityDTO implements UserDetails {

    private String userId;
    private String userPw;
    private String userEmail;
    private boolean enabled; // 유저 상태
    private Character userSocial; // 'K' for Kakao, 'N' for Naver, 'G' for Google, etc.
    private Map<String,Object> props; // 소셜로그인 정보
    private Collection<? extends GrantedAuthority> authorities;

    public UserSecurityDTO(String userId, String password, String email,
                           boolean enabled, Character social,
                           Collection<? extends GrantedAuthority> authorities) {
        this.userId = userId;
        this.userPw = password;
        this.userEmail = email;
        this.enabled = enabled;
        this.userSocial = social;
        this.authorities = authorities;
    }

    // 만약 소셜 로그인 정보를 반환하는 커스텀 메서드가 필요하다면, 별도로 정의합니다.
    public Map<String,Object> getAttributes(){
        return this.props;
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