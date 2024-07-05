package com.momo.momopjt.user;


import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
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

    private int calculateAge(LocalDate birthDate) {
        return Period.between(birthDate, LocalDate.now()).getYears();
    }

    @Override
    @Transactional
    public void updateUser(UserUpdateDTO userUpdateDTO) {
        User user = userRepository.findByUserId(userUpdateDTO.getUserId());
        if (user == null) {
            throw new IllegalArgumentException("User not found with userId: " + userUpdateDTO.getUserId());
        }
        user.changePassword(passwordEncoder.encode(userUpdateDTO.getUserPw()));
        user.changeEmail(userUpdateDTO.getUserEmail());
        user.changeNickname(userUpdateDTO.getUserNickname());
        user.changeUserCategory(userUpdateDTO.getUserCategory());
        user.changeAddress(userUpdateDTO.getUserAddress());
        user.changeUserMbti(userUpdateDTO.getUserMBTI());
        user.setUserModifyDate(Instant.now());

        userRepository.save(user);

    }
}