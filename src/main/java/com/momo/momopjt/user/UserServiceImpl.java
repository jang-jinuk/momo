package com.momo.momopjt.user;


import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


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
}