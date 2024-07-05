package com.momo.momopjt.user;


public interface UserService {
    void updateUser(UserJoinDTO userJoinDTO, User user);
    static class UserIdException extends Exception{
    }
    void join(UserJoinDTO userJoinDTO)throws UserIdException;

    //Id Email 증복 체크
    boolean checkUserIdDuplicate(String userId);
    boolean checkUserEmailDuplicate(String userEmail);
    //serviceImpe에서 계속
}

