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
    public boolean isUserIdTaken(String userId) {
        return userRepository.findByUserId(userId).isPresent();
    }

    @Override
    public boolean isUserEmailTaken(String userEmail) {
        return userRepository.findByUserEmail(userEmail).isPresent();
    }

    @Override
    public void registerUser(UserDto userDto){
        if(isUserIdTaken(userDto.getUserId())){
            throw new IllegalArgumentException("증복된 아아아아아아이디디디 대왕");
        }
        if(isUserEmailTaken(userDto.getUserEmail())){
            throw new IllegalArgumentException("증복된 이메일! 사라져라!");
        }

        User user = new User();
        user.setUserId(user.getUserId());
        user.setUserEmail(user.getUserEmail());

        userRepository.save(user);
    }
}