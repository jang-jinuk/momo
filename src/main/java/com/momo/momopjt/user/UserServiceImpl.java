package com.momo.momopjt.user;


import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Log4j2
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {


    private final ModelMapper modelMapper;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void join(UserJoinDTO userJoinDTO) throws UserIdException {

        String userId = userJoinDTO.getUserId();

        boolean exist = userRepository.existsByUserId(userId); // existsByUserId 사용
        if (exist) {
            throw new UserIdException();
        }

        User user = modelMapper.map(userJoinDTO, User.class);
        user.changePassword(passwordEncoder.encode(userJoinDTO.getUserPw()));
        user.addRole(UserRole.USER);

        log.info("===============");
        log.info(user);
        log.info(user.getRoleSet());

        userRepository.save(user);
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