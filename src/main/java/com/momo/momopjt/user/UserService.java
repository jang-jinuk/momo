package com.momo.momopjt.user;


public interface UserService {

    static class UserIdException extends Exception{

    }

    void join(UserJoinDTO userJoinDTO)throws UserIdException;
    //void register(UserDto userDto);
}

