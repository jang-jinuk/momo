package com.momo.momopjt.user;


public interface UserService {

    void join(UserJoinDTO userJoinDTO) throws UserIdException, UserEmailException;

    void updateUser(UserUpdateDTO userUpdateDTO);

    class UserIdException extends Exception {
        // 추가적인 예외 처리가 필요한 경우에만 사용
    }
    class UserEmailException extends Exception{
    }
}