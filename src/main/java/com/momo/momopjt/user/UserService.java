package com.momo.momopjt.user;


public interface UserService {

    void updateUser(UserJoinDTO userJoinDTO, User user);

    static class UserIdException extends Exception{

    }

    void join(UserJoinDTO userJoinDTO)throws UserIdException;


}

