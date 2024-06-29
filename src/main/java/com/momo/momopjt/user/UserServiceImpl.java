/*package com.momo.momopjt.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;
    @Override
    public void register(UserDto userDto) {

        User user = new User();

        user.setUserAddress(userDto.getUserAddress());
//        쭉쭉추가추가추가

        userRepository.save(user);

    }
}

*/

