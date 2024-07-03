package com.momo.momopjt.user;


public interface UserService {

    static class UserIdException extends Exception{}
    static class UserPwException extends Exception{}
    static class UserNickException extends Exception{}
    static class UserEmailException extends Exception{}
    static class UserMbtiException extends Exception{}

    void join(UserJoinDTO userJoinDTO)
        throws
        UserIdException,
        UserPwException,
        UserNickException,
        UserEmailException,
        UserMbtiException;

    //void register(UserDto userDto);
}

