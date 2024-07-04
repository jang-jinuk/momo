package com.momo.momopjt;

import com.momo.momopjt.security.CustomUserDetailService;
import com.momo.momopjt.user.*;
import lombok.extern.log4j.Log4j2;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.Commit;
import org.springframework.test.context.ContextConfiguration;
import java.util.regex.Pattern;
import javax.transaction.Transactional;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Collection;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.IntStream;

import static com.momo.momopjt.user.QUser.user;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@Log4j2
@ContextConfiguration(classes = MomoApplication.class)
public class UserTests {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    //비밀번호 암호화
    private PasswordEncoder passwordEncoder;
    private CustomUserDetailService customUserDetailService;

    @Test
        //회원 추가 테스트
    void insertUserTests() {

        IntStream.rangeClosed(1, 100).forEach(i -> {

            User user = User.builder()
                .userId("user" + i)
                .userPw(passwordEncoder.encode("1111"))
                .userEmail("email" + i + "@aaa.bbb")
                .build();

            user.addRole(UserRole.USER);

            if (i >= 90) {
                user.addRole(UserRole.ADMIN);
            }
            userRepository.save(user);

        });
    }

    @Test
    void 회원가입() {
        //시간 설정
        Instant now = Instant.now();

        //시간 설정 2

        LocalDate date = LocalDate.parse("2016-10-31", DateTimeFormatter.ISO_DATE);
        System.out.println(date);
        log.info("-------- [06-28-11:17:45]-------you");

        User user = User.builder()

            .userNo(3L) //LONG타입
            .userId("tete1")  //String 타입
            .userPw("1234")  //String 타입
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

    @Test
    @Transactional
    public void 회원조회테스트() {
        // socialTypes 컬렉션 생성
        Collection<Character> socialTypes = Arrays.asList('k', 'n', 'g', 'm');

        // findByUserIdAndUserSocialIn 메서드 호출
        Optional<User> result = userRepository.findByUserIdAndUserSocialIn("dbwjd1234", socialTypes);
        User user = result.orElseThrow();

        log.info(user.toString());
        log.info(user.getRoleSet().toString());

        user.getRoleSet().forEach(userRole -> log.info(userRole.name()));
    }

    @Test
    @Transactional
    public void 회원탈퇴테스트() {
        // 테스트에 사용할 userId와 socialTypes 설정
        String userId = "dbwjd1234";
        Collection<Character> socialTypes = Arrays.asList('k', 'n', 'g', 'm');

        // findByUserIdAndUserSocialIn 메서드 호출하여 Optional<User> 반환
        Optional<User> result = userRepository.findByUserIdAndUserSocialIn(userId, socialTypes);

        // Optional이 비어있을 경우 예외를 던짐
        User user = result.orElseThrow(() -> new NoSuchElementException("회원 정보가 존재하지 않습니다."));

        // 사용자를 삭제
        userRepository.deleteById(user.getUserNo());

        // 사용자 삭제 여부 확인
        Optional<User> deleteResult = userRepository.findById(user.getUserNo());
        Assertions.assertThat(deleteResult).isEmpty(); // AssertJ를 사용하여 Optional이 비어있는지 확인

        // 삭제된 사용자를 다시 찾을 때 예외가 발생하는지 확인 (선택 사항)
        assertThrows(NoSuchElementException.class, () -> userRepository.findById(user.getUserNo()).orElseThrow(() -> new NoSuchElementException("삭제된 사용자를 찾을 수 없습니다.")));
    }

    @Commit
    @Test
    public void 카카오톡소셜테스트(){
        String userId = "ppppp1p@navr.com";
        String userPw = passwordEncoder.encode("1234");

        userRepository.updatePassword(userId, userPw);
    }
        @Test // SW 유효성 검사용 테스트기
        @DisplayName("정규 표현식 검사 TRUE/FALSE")
        public void okORnot() {

String pattern = "^(?=.*[a-zA-Z])(?=.*\\d)[a-zA-Z0-9]{6,12}$"; //정규식
String val = "dmgo235"; //판별될 놈

            boolean regex = Pattern.matches(pattern, val); // 맞는지 아닌지 T/F
            log.info("...... [07-02-19:29:25]..........KSW");
            System.out.println(regex);
            log.info("...... [07-02-19:29:20]..........KSW");
        }

}