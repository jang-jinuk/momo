package com.momo.momopjt.user;

import com.momo.momopjt.MomoApplication;
import com.momo.momopjt.global.security.CustomUserDetailService;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ContextConfiguration;

import javax.transaction.Transactional;
import java.time.Instant;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;
import java.util.stream.IntStream;


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

        LocalDate date = LocalDate.parse("2011-11-11", DateTimeFormatter.ISO_DATE);
        System.out.println(date);
        log.info("-------- [06-28-11:17:45]-------you");

        User user = User.builder()

            .userNo(-1L) //LONG타입
            //자동 추가되게끔 -1L로 설정 YY
            .userId("dbwjd1234")  //String 타입
            .userPw(passwordEncoder.encode("dbwjd"))  //String 타입
            // password encoding 되게 수정 YY
            .userNickname("momoguy1")  //String 타입
            .userEmail("email1@momo.com") //String 타입
            .userGender('m') //char타입
//            .userAge(30) //Integer타입
            //자동으로 계산되므로 나이 주석처리 YY
            .userBirth(date)  //LocalDate 타입
            .userCategory("game") //String 타입
            .userAddress("Seoul") //String 타입
            .userMBTI("INTP") //String 타입
            .userState('0') //char타입
            .userSocial('m') //char타입
            .userPhoto("userDefault.jpg") //string타입
            .userLikeNumber(0) //integer타입
            .userCreateDate(now) //instant타입
//            .userModifyDate() //instant타입
            // update가 아니라 생성이므로 modify 주석처리
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

    }

    //    @Test
//        //회원 추가 테스트
//    void insertUserTests() {
//        IntStream.rangeClosed(1, 100).forEach(i -> {
//
//            User user = User.builder()
//                .userId("user" + i)
//                .userPw(passwordEncoder.encode("1111"))
//                .userEmail("email" + i + "@aaa.bbb")
//                .build();
//            user.addRole(UserRole.USER);
//
//            if (i >= 90) {
//                user.addRole(UserRole.ADMIN);
//            }
//            userRepository.save(user);
//        });
//    }
//
//    @Test
//    void 회원가입() {
//        //시간 설정
//        Instant now = Instant.now();
//        //시간 설정 2
//        LocalDate date = LocalDate.parse("2016-10-31", DateTimeFormatter.ISO_DATE);
//
//        System.out.println(date);
//        log.info("-------- [06-28-11:17:45]-------you");
//
//        User user = User.builder()
//
//            .userNo(3L) //LONG타입
//            .userId("tete1")  //String 타입
//            .userPw("1234")  //String 타입
//            .userNickname("momoguy1")  //String 타입
//            .userEmail("email1@momo.com") //String 타입
//            .userGender('m') //char타입
//            .userAge(30) //Integer타입
//            .userBirth(date)  //LocalDate 타입
//            .userCategory("game") //String 타입
//            .userAddress("Seoul") //String 타입
//            .UserMBTI("INTP") //String 타입
//            .userState('O') //char타입
//            .userSocial('m') //char타입
//            .userPhoto("") //string타입
//            .userLikeNumber(0) //integer타입
//            .userCreateDate(now) //instant타입
//            .userModifyDate(now) //instant타입
//            .build();
//
//        userRepository.save(user);
//    }
//
//    @Test
//    @Transactional
//    public void 회원조회테스트() {
//        // socialTypes 컬렉션 생성
//        Collection<Character> socialTypes = Arrays.asList('k', 'n', 'g', 'm');
//
//        // findByUserIdAndUserSocialIn 메서드 호출
//        Optional<User> result = userRepository.findByUserIdAndUserSocialIn("dbwjd1234", socialTypes);
//        User user = result.orElseThrow();
//
//        log.info(user.toString());
//        log.info(user.getRoleSet().toString());
//
//        user.getRoleSet().forEach(userRole -> log.info(userRole.name()));
//    }
//
//    @Test
//    @Transactional
//    public void 회원탈퇴테스트() {
//        // 테스트에 사용할 userId와 socialTypes 설정
//        String userId = "dbwjd1234";
//        Collection<Character> socialTypes = Arrays.asList('k', 'n', 'g', 'm');
//
//        // findByUserIdAndUserSocialIn 메서드 호출하여 Optional<User> 반환
//        Optional<User> result = userRepository.findByUserIdAndUserSocialIn(userId, socialTypes);
//
//        // Optional이 비어있을 경우 예외를 던짐
//        User user = result.orElseThrow(() -> new NoSuchElementException("회원 정보가 존재하지 않습니다."));
//
//        // 사용자를 삭제
//        userRepository.deleteById(user.getUserNo());
//
//        // 사용자 삭제 여부 확인
//        Optional<User> deleteResult = userRepository.findById(user.getUserNo());
//        Assertions.assertThat(deleteResult).isEmpty(); // AssertJ를 사용하여 Optional이 비어있는지 확인
//
//        // 삭제된 사용자를 다시 찾을 때 예외가 발생하는지 확인 (선택 사항)
//        assertThrows(NoSuchElementException.class, () -> userRepository.findById(user.getUserNo()).orElseThrow(() -> new NoSuchElementException("삭제된 사용자를 찾을 수 없습니다.")));
//    }
//
//    @Commit
//    @Test
//    public void 카카오톡소셜테스트() {
//        String userId = "ppppp1p@navr.com";
//        String userPw = passwordEncoder.encode("1234");
//
//        userRepository.updatePassword(userId, userPw);
//    }
//
//    @Test // SW 유효성 검사용 테스트기
//    @DisplayName("정규 표현식 검사 TRUE/FALSE")
//    public void okORnot() {
//
//        String pattern = "^[a-zA-Z0-9]+@([\\w-]+\\.)+[\\w-]{2,5}$"; //정규식
//        String val = "rlatlsdn34@daum.net.df"; //판별될 놈
//
//        boolean regex = Pattern.matches(pattern, val); // 맞는지 아닌지 T/F
//        log.info("...... [07-02-19:29:25]..........KSW");
//        System.out.println(regex);
//        log.info("...... [07-02-19:29:20]..........KSW");
//    }
//    @Test
//    public void EmailExistCheck() {
//        // 테스트할 유저
//        // Mapper;
//        User testuser = userRepository.findById(1L).orElseThrow();
//        UserDTO userDTO = modelMapper.map(testuser, UserDTO.class);
//
//        // 테스트 초기값 세팅
//        final String EmailOne = userDTO.getUserEmail(); // 검사할 이메일 값을 EmailOne에 저장
//        boolean checkresult = false;
//
//        // DB의 모든 회원을 리스트로 받아온다
//        List<User> userList = userRepository.findAll(); // List<User> 반환
//
//        // 이메일 중복 여부를 확인
//        for (User user : userList) {
//            String EmailTwo = user.getUserEmail();
//            if (EmailOne.equals(EmailTwo)) {
//                checkresult = true;
//                break; // 중복된 이메일을 찾으면 더 이상 반복할 필요 없음
//            }
//        }
//        log.info("----------------- [07-05 17:56:24]-----------------");
//        log.info(checkresult);
//    }
//
//    이메일 증복 확인
//    @Test
//    void TestEmail(){
//        log.info("...... [이메일 중복확인 테스트 시작]..........KSW");
//        String email1 = "asdf1234@naver.com"; // 이미 존재하는 이메일 입력
//        Optional<User> testuser = userRepository.findByUserEmail(email1);
//        log.info(testuser.orElseThrow().toString()); // 확인한 이메일을 가지는 User 정보 출력
//        log.info(testuser.orElseThrow().getUserEmail()); // 확인한 이메일 출력
//        log.info("...... [이메일 중복확인 테스트 끝]..........KSW");
//
//    }
//
}
