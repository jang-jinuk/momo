package com.momo.momopjt.user;

public interface UserService {
    static class UserIdException extends Exception{
    }
    void join(UserJoinDTO userJoinDTO)throws UserIdException;

    boolean isUserIdTaken(String userId);
    boolean isUserEmailTaken(String userEmail);
}

