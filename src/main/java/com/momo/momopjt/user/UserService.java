package com.momo.momopjt.user;


import java.util.Optional;

public interface UserService {

    void join(UserDTO userDTO) throws UserIdException;

    void updateUser(UserDTO userDTO);

    static class UserIdException extends Exception {
        // 추가적인 예외 처리가 필요한 경우에만 사용
    }
}