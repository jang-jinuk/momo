package com.momo.momopjt.user;


import java.util.Optional;

public interface UserService {

    void join(UserDTO userDTO) throws UserIdException, UserEmailException;

    void updateUser(UserDTO userDTO);

    String findUsernameByEmail(String userEmail);

    boolean resetPassword(String userId, String userEmail, String newPassword);

    User findByEmail(String userEmail);

    User findByUserId(String userId);

    String generateTemporaryPassword(); // 임시 비밀번호 생성 메소드 추가

    void updateUserPassword(User user, String temporaryPassword);

    boolean resetPasswordByUserId(String userId, String newPassword);
    static class UserIdException extends Exception {
        // 추가적인 예외 처리가 필요한 경우에만 사용
    }
    class UserEmailException extends Exception{
    }

    User findByUserIdAndUserEmail(String userId, String userEmail);

}