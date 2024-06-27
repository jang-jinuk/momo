package com.momo.momopjt;

import com.momo.momopjt.user.User;
import com.momo.momopjt.user.UserDto;
import com.momo.momopjt.user.UserRepository;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootTest
@Log4j2
public class UserTests {

    @Autowired
    private UserRepository userRepository;

    @Autowired
        //비밀번호 암호화
    private PasswordEncoder passwordEncoder;
    @Test
        //회원 추가 테스트
    void insertUserTests(){
        UserDto user = UserDto.builder()
            .userNo(1L)
            .userId("user1")
            .userPw("1234")
            .userNickname("momoguy1")
            .userEmail("email1@momo.com")
            .userGender("M")
            .userAge(30)
            .userBirth("2001:08:22")
            .userCategory("game")
            .userAddress("Seoul")
            .userMbti("INTP")
            .userState("good")
            .userSocial()
            .userPhoto()
            .userLikeNumber(0)
            .userCreateDate()
            .userModifyDate()
            .userAdmin()
            .build();
    userService.(user);
        };

    @Test
    //회원 조회 테스트
    void testReadUserTests() {
        userService.read()
    }
}

