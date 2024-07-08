package com.momo.momopjt.user;


import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.dao.DataAccessException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.time.Instant;
import java.time.LocalDate;
import java.time.Period;



@Log4j2
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final ModelMapper modelMapper;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    @Override
    @Transactional
    public void join(UserDTO userDTO) throws UserIdException {

        String userId = userDTO.getUserId();

        boolean exist = userRepository.existsByUserId(userId); // existsByUserId 사용
        if (exist) {
            throw new UserIdException();
        }

        User user = modelMapper.map(userDTO, User.class);

        // 비밀번호 암호화
        user.changePassword(passwordEncoder.encode(userDTO.getUserPw()));

        // 역할 설정
        user.addRole(UserRole.USER);

        // 나이 계산
        int userAge = calculateAge(userDTO.getUserBirth());
        user.setUserAge(userAge);

        // 현재 날짜 설정
        Instant now = Instant.now();
        user.setUserCreateDate(now);
        user.setUserModifyDate(now);

        // 기본 값 설정
        user.setUserState('0');
        user.setUserLikeNumber(0);

        log.info(user);
        log.info(user.getRoleSet());

        userRepository.save(user);
    }

    //가입된 생년월일로 데이터베이스에 age항목에 넣는다.
    private int calculateAge(LocalDate birthDate) {
        return Period.between(birthDate, LocalDate.now()).getYears();
    }

    @Override
    public void updateUser(UserDTO userDTO) {
        // 1. 사용자 찾기
        User user = userRepository.findByUserId(userDTO.getUserId());
        if (user == null) {
            throw new IllegalArgumentException("User not found with userId: " + userDTO.getUserId());
        }

        // 2. 비밀번호 업데이트
        if (userDTO.getUserPw() != null && !userDTO.getUserPw().isEmpty()) {
            user.setUserPw(passwordEncoder.encode(userDTO.getUserPw()));
        }
        // 3. 이메일 업데이트
        if (userDTO.getUserEmail() != null && !userDTO.getUserEmail().isEmpty()) {
            user.setUserEmail(userDTO.getUserEmail());
        }

        // 4. 닉네임 업데이트
        if (userDTO.getUserNickname() != null && !userDTO.getUserNickname().isEmpty()) {
            user.setUserNickname(userDTO.getUserNickname());
        }

        // 5. 카테고리 업데이트
        if (userDTO.getUserCategory() != null && !userDTO.getUserCategory().isEmpty()) {
            user.setUserCategory(userDTO.getUserCategory());
        }

        // 6. 주소 업데이트
        if (userDTO.getUserAddress() != null && !userDTO.getUserAddress().isEmpty()) {
            user.setUserAddress(userDTO.getUserAddress());
        }

        // 7. MBTI 업데이트
        if (userDTO.getUserMBTI() != null && !userDTO.getUserMBTI().isEmpty()) {
            user.setUserMBTI(userDTO.getUserMBTI());
        }

        // 8. 소셜 타입 업데이트
        //if (userDTO.getUserSocial() != null) {
        //   user.setUserSocial(userDTO.getUserSocial());
        //}

        // 9. 수정일 업데이트
        user.setUserModifyDate(Instant.now());

        // 10. 변경사항 저장
        try {
            userRepository.save(user);
        } catch (DataAccessException e) {
            // 데이터베이스 저장 중 문제가 발생한 경우 처리
            throw new RuntimeException("Failed to update user with userId: " + user.getUserId(), e);
        }
    }


}