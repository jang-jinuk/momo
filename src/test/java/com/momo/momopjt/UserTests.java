package com.momo.momopjt;

import com.momo.momopjt.user.*;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.stream.IntStream;

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

        IntStream.rangeClosed(1,100).forEach(i -> {

            User user = User.builder()
                    .userId("user"+i)
                    .userPw(passwordEncoder.encode("1111"))
                    .userEmail("email"+i+"@aaa.bbb")
                            .build();

            user.addRole(UserRole.USER);

            if(i >= 90){
                user.addRole(UserRole.ADMIN);
            }
            userRepository.save(user);

        });
    }

    @Test
    void 회원가입 (){
        //시간 설정
        Instant now = Instant.now();

        //시간 설정 2

        LocalDate date = LocalDate.parse("2016-10-31", DateTimeFormatter.ISO_DATE);
        System.out.println(date);
        log.info("-------- [06-28-11:17:45]-------you");

        User user = User.builder()

                .userNo(2L) //LONG타입
                .userId("user1412")  //String 타입
                .userPw("12343")  //String 타입
                .userNickname("momoguy1")  //String 타입
                .userEmail("email1@momo.com") //String 타입
                .userGender('m') //char타입
                .userAge(30) //Integer타입
                .userBirth(date)  //LocalDate 타입
                .userCategory("game") //String 타입
                .userAddress("Seoul") //String 타입
                .userMbti("INTP") //String 타입
                .userState('O') //char타입
                .userSocial('m') //char타입
                .userPhoto("") //string타입
                .userLikeNumber(0) //integer타입
                .userCreateDate(now) //instant타입
                .userModifyDate(now) //instant타입
                .build();

        userRepository.save(user);
    }
}

