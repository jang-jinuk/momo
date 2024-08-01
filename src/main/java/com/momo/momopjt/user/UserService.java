package com.momo.momopjt.user;


import java.util.List;
import java.util.Optional;
public interface UserService {

  Optional<User> findByUserNo(Long userNo);

  void signup(UserDTO userDTO) throws UserIdException, UserEmailException, UserNicknameException;

  void updateUser(UserDTO userDTO);

  String findUsernameByEmail(String userEmail);

  boolean resetPassword(String userId, String userEmail, String newPassword);

  User findByEmail(String userEmail);

  User findByUserId(String userId);

  String generateTemporaryPassword(); // 임시 비밀번호 생성 메소드 추가

  void updateUserPassword(User user, String temporaryPassword);

  boolean resetPasswordByUserId(String userId, String newPassword);

  User findByUserIdAndUserEmail(String userId, String userEmail);

  void deleteAccount(String userId, String userPw);

  User getCurrentUser();

  // 추가적인 예외 처리가 필요한 경우에만 사용
  class UserEmailException extends Exception {}

  class UserIdException extends Exception {}

  class UserNicknameException extends Exception {}
  //관리자 유저 전체 조회
  List<UserDTO> readALLUsers();
  //관리자 유저 검색
  List<UserDTO> searchUsers(String query);
}