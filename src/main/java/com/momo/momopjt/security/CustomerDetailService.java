package com.momo.momopjt.security;


import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Log4j2
@Service
// @RequiredArgsConstructor
public class CustomerDetailService implements UserDetailsService {

    //임시코드
    private PasswordEncoder pe;

    public CustomerDetailService(){
        this.pe = new BCryptPasswordEncoder();
    }

    @Override
    public UserDetails loadUserByUsername(String customerName) throws
            UsernameNotFoundException {
        log.info("loadUserByUsername: " + customerName);


        UserDetails userDetails = User.builder().username("user1")

                //.password("1111")
                .password(pe.encode("1111")) // 패스워드인코딩필요
                .authorities("ROLE_USER")
                .build();

        return userDetails;
    }
}
