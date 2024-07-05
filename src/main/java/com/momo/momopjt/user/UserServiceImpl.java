package com.momo.momopjt.user;


import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotNull;
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
    public void join(UserJoinDTO userJoinDTO) throws UserIdException {

        String userId = userJoinDTO.getUserId();

        boolean exist = userRepository.existsByUserId(userId); // existsByUserId 사용
        if (exist) {
            throw new UserIdException();
        }

        User user = modelMapper.map(userJoinDTO, User.class);

        // 비밀번호 암호화
        user.changePassword(passwordEncoder.encode(userJoinDTO.getUserPw()));

        // 역할 설정
        user.addRole(UserRole.USER);

        // 나이 계산
        int userAge = calculateAge(userJoinDTO.getUserBirth());
        user.setUserAge(userAge);

        // 현재 날짜 설정
        Instant now = Instant.now();
        user.setUserCreateDate(now);
        user.setUserModifyDate(now);

        // 기본 값 설정
        user.setUserState('0');
        user.setUserLikeNumber(0);

        log.info("===============");
        log.info(user);
        log.info(user.getRoleSet());

        userRepository.save(user);
    }
    //Id Email 체크
    @Override
    public boolean checkUserIdDuplicate(String userId) {
        return userRepository.existsUserByUserId(userId);
    }
    @Override
    public boolean checkUserEmailDuplicate(String userEmail) {
        return userRepository.existsUserByUserEmail(userEmail);
    }


    private int calculateAge(@NotNull(message = "Birth date is required") LocalDate birthDate) {
        return Period.between(birthDate, LocalDate.now()).getYears();
    }

    @Override
    public void updateUser(UserJoinDTO userJoinDTO, User user) {
        // userJoinDTO에서 업데이트할 사용자 정보 추출
        String userNickname = userJoinDTO.getUserNickname();
        String userEmail = userJoinDTO.getUserEmail();
        String userPw = userJoinDTO.getUserPw();
        String userCategory = userJoinDTO.getUserCategory();
        String userAddress = userJoinDTO.getUserAddress();
        String userMbti = userJoinDTO.getUserMbti();

        // user 객체의 정보 업데이트
        if (userNickname != null) user.setUserNickname(userNickname);
        if (userEmail != null) user.setUserEmail(userEmail);
        if (userPw != null) user.setUserPw(userPw);
        if (userCategory != null) user.setUserCategory(userCategory);
        if (userAddress != null) user.setUserAddress(userAddress);
        if (userMbti != null) user.setUserMbti(userMbti);

        // 업데이트된 사용자 정보를 데이터베이스에 저장하는 등의 로직 작성
        userRepository.save(user);
    }

}